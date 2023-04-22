package com.kartheek.weatherapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartheek.weatherapp.common.datastore.MyDataStore
import com.kartheek.weatherapp.data.NetworkResult
import com.kartheek.weatherapp.data.model.WeatherRes
import com.kartheek.weatherapp.data.repository.WeatherDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
 private val weatherRepository: WeatherDataRepository,
 private var dataStore: MyDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetworkResult<WeatherRes>>(NetworkResult.Loading())

    val uiState: StateFlow<NetworkResult<WeatherRes>> = _uiState

    var searchFieldValue by mutableStateOf("")
        private set

    var isCitySearched by mutableStateOf(false)
        private set

    var cityName =""

    fun updateSearchField(input: String) {
        searchFieldValue = input
    }


    fun searchCityClick(){
        isCitySearched = true
        viewModelScope.launch {
            if (checkSearchFieldValue()) {
                dataStore.setCityName(searchFieldValue)
                _uiState.value = NetworkResult.Loading()
                weatherRepository.getWeatherData(searchFieldValue)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        // handle exception
                        _uiState.value = NetworkResult.Error(e.toString())
                    }
                    .collect {
                        // list of users from the network
                        _uiState.value = it
                    }
            }

        }
    }
    fun searchWithCurrentLocation(cityName:String){
        isCitySearched = true
        viewModelScope.launch {
                _uiState.value = NetworkResult.Loading()
                weatherRepository.getWeatherData(cityName)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        // handle exception
                        _uiState.value = NetworkResult.Error(e.toString())
                    }
                    .collect {
                        // list of users from the network
                        _uiState.value = it
                    }

        }
    }

    fun autoLoadWeather(){
        try {
                if(getLastCityNameSearched().isNotEmpty()){
                    viewModelScope.launch {
                    isCitySearched = true
                    _uiState.value = NetworkResult.Loading()
                    weatherRepository.getWeatherData(getLastCityNameSearched())
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            // handle exception
                            _uiState.value = NetworkResult.Error(e.toString())
                        }
                        .collect {
                            // list of users from the network
                            _uiState.value = it
                        }

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkSearchFieldValue(): Boolean {
        return searchFieldValue.isNotEmpty()
    }

    fun getLastCityNameSearched():String{
        viewModelScope.launch {
           cityName = dataStore.getCityName()
        }
        return cityName
    }
}