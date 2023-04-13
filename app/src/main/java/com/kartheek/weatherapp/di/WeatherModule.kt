package com.kartheek.weatherapp.di

import com.kartheek.weatherapp.data.repository.WeatherDataRepository
import com.kartheek.weatherapp.data.repository.WeatherDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class WeatherModule {

    @Binds
    abstract fun bindWeatherInterface(
        weatherDataRepositoryImpl: WeatherDataRepositoryImpl
    ): WeatherDataRepository
}