package com.kartheek.weatherapp.common.datastore

import kotlinx.coroutines.flow.Flow

interface IMyDataStore {
  suspend  fun setCityName(value:String)
    suspend  fun getCityName(): String

}