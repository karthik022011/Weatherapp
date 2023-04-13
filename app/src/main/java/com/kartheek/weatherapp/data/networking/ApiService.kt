package com.kartheek.weatherapp.data.networking

import com.kartheek.weatherapp.data.model.WeatherRes
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET
    suspend fun getWeather(): Response<WeatherRes>
}