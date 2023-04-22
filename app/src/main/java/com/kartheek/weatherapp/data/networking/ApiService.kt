package com.kartheek.weatherapp.data.networking

import com.kartheek.weatherapp.common.utils.MyAppConstants
import com.kartheek.weatherapp.data.model.WeatherRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(MyAppConstants.WEATHER_END_POINT)
    suspend fun getWeatherWithCityName(
        @Query("q") cityName: String,
        @Query("APPID") apiKey: String = MyAppConstants.API_KEY,
        @Query("units") units: String = MyAppConstants.UNITS
    ): Response<WeatherRes>
}