package com.kartheek.weatherapp.presenter


import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.kartheek.weatherapp.common.hasPermission
import com.kartheek.weatherapp.common.isLocationEnabled
import com.kartheek.weatherapp.common.showToast
import com.kartheek.weatherapp.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest

    private val fusedLocationProviderClient : FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val geocoder: Geocoder by lazy {
        Geocoder(this)
    }

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){permission ->
        when{
            permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                        checkPermission()
                    }
            else ->{
                //No location access granted
                showToast("To get current location, please allow location access")
                viewModel.autoLoadWeather()
            }
        }

    }

    val resolutionForResult =   registerForActivityResult(
    ActivityResultContracts.StartIntentSenderForResult()){activityResult ->
        when(activityResult.resultCode){
            RESULT_OK ->{
                checkPermission()
            }
            RESULT_CANCELED ->{
                showToast("To get current location, please allow location access")
                viewModel.autoLoadWeather()
            }
            else ->{
                showToast("To get current location, please allow location access")
                viewModel.autoLoadWeather()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WeatherScreen(viewModel)
                }
            }
        }
        initLocalRequest()
        checkPermission()
    }
    

    fun initLocalRequest(){
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10*10000)
            .setWaitForAccurateLocation(false)
            .build()
    }

    private fun checkPermission(){
      if(hasPermission()){
          if(isLocationEnabled()){
             getLocationUpdates()
          }else{
            requestDeviceLocationSettings()
          }
      }else{
          if(shouldShowRationale()){
            showToast("To get current location, please allow location access")
          }else{
              requestPermission()
          }
      }
    }

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(){
     locationCallback = object : LocationCallback(){
         override fun onLocationResult(result: LocationResult) {
             super.onLocationResult(result)
             result.lastLocation.let {
                 it?.let {location ->
                     getAddress(getLatLong(location.latitude,location.longitude))
                 }
             }
         }
     }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    fun getLatLong(latitude:Double, longitude:Double): LatLng{
        return LatLng(latitude,longitude)
    }

    fun getAddress(latLng: LatLng){
        if(Build.VERSION.SDK_INT >= 33){
            geocoder.getFromLocation(latLng.latitude, latLng.longitude,1, object:Geocoder.GeocodeListener{
                override fun onGeocode(address: MutableList<Address>) {
                   if(address.isNotEmpty()){
                       val fetch = address[0]
                       viewModel.searchWithCurrentLocation(fetch.locality)
                   }
                }

            })
        }else{
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1)
            address?.let {
                if(address.isNotEmpty()){
                    val fetch = address[0]
                    viewModel.searchWithCurrentLocation(fetch.locality)
                }
            }
        }
    }



    private fun shouldShowRationale():Boolean{
        return ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestPermission(){
     locationPermissionRequest.launch(arrayOf(
         Manifest.permission.ACCESS_COARSE_LOCATION,
         Manifest.permission.ACCESS_FINE_LOCATION
     ))
    }



    fun requestDeviceLocationSettings(){
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {locationSettingsResponse ->
            //If GPS is already enabled then this listener will be invoked
            showToast("GPS is Enabled")
        }
        task.addOnFailureListener{ exception ->
            //If GPS is not enabled then this listener will be invoked
            if (exception is ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                   val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()

                }
            }
        }
    }



}