package com.kartheek.weatherapp.common.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*


val Context.myDataStore by preferencesDataStore(name ="weather_data_store")

class MyDataStore(var context :Context) : IMyDataStore {

    override suspend fun setCityName(value: String) {
        context.myDataStore.edit {
            it[KEY_City_Name] = value
        }
    }

    override suspend fun getCityName() :Flow<String> = context.myDataStore.data
        .map {
            it[KEY_City_Name].toString()
        }


    companion object{
        val KEY_City_Name = stringPreferencesKey("key_city_name")
    }
}