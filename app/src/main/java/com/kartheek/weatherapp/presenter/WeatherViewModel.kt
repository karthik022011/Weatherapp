package com.kartheek.weatherapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartheek.weatherapp.data.NetworkResult
import com.kartheek.weatherapp.data.model.WeatherRes
import com.kartheek.weatherapp.data.repository.WeatherDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
 private val weatherRepository: WeatherDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetworkResult<WeatherRes>>(NetworkResult.Loading())

    val uiState: StateFlow<NetworkResult<WeatherRes>> = _uiState

    fun fetchWeatherData(){
        viewModelScope.launch {
            weatherRepository.getWeatherData()
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