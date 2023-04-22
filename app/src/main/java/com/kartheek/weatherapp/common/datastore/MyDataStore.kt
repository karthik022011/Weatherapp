package com.kartheek.weatherapp.common.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException


val Context.myDataStore by preferencesDataStore(name ="weather_data_store")

class MyDataStore(var context :Context) : IMyDataStore {

    override suspend fun setCityName(value: String) {
        context.myDataStore.edit {
            it[KEY_City_Name] = value
        }
    }

    override suspend fun getCityName() :String =
        runBlocking {
            context.myDataStore.data.map {
                it[KEY_City_Name]
            }.first().toString()
        }


    companion object{
        val KEY_City_Name = stringPreferencesKey("key_city_name")
    }
}