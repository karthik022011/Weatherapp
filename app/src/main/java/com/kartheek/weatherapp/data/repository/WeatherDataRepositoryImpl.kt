package com.kartheek.weatherapp.data.repository

import com.kartheek.weatherapp.data.NetworkResult
import com.kartheek.weatherapp.data.model.WeatherRes
import com.kartheek.weatherapp.data.networking.ApiService
import com.kartheek.weatherapp.common.utils.BaseApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WeatherDataRepositoryImpl @Inject constructor(
    private val apiService : ApiService
) : WeatherDataRepository, BaseApiResponse() {

    override suspend fun getWeatherData(cityName:String): Flow<NetworkResult<WeatherRes>> {
        return flow<NetworkResult<WeatherRes>> {
            emit(safeApiCall { apiService.getWeatherWithCityName(cityName) })
        }.flowOn(Dispatchers.IO)

    }
}