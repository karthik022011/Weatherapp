package com.kartheek.weatherapp.data.repository

import com.kartheek.weatherapp.data.NetworkResult
import com.kartheek.weatherapp.data.model.WeatherRes
import kotlinx.coroutines.flow.Flow

interface WeatherDataRepository {

  suspend fun getWeatherData(cityName:String): Flow<NetworkResult<WeatherRes>>

}