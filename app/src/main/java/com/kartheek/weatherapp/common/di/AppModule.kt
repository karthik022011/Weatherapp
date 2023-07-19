package com.kartheek.weatherapp.common.di

import android.content.Context
import com.kartheek.weatherapp.BuildConfig
import com.kartheek.weatherapp.ForceCacheInterceptor
import com.kartheek.weatherapp.common.CacheInterceptor
import com.kartheek.weatherapp.data.networking.ApiService
import com.kartheek.weatherapp.common.utils.MyAppConstants
import com.kartheek.weatherapp.common.datastore.MyDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = MyAppConstants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context) = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(Cache(File(appContext.cacheDir, "http-cache"), 10L * 1024L * 1024L))
            .addInterceptor(ForceCacheInterceptor(appContext))
            .addNetworkInterceptor(CacheInterceptor())
            .build()
    }else{
        OkHttpClient.Builder()
            .cache(Cache(File(appContext.cacheDir, "http-cache"), 10L * 1024L * 1024L))
            .addInterceptor(ForceCacheInterceptor(appContext))
            .addNetworkInterceptor(CacheInterceptor())
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit{
       return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit:Retrofit) : ApiService{
        return retrofit.create(ApiService::class.java)
    }
    @Singleton
    @Provides
    fun  provideMyDataStore(@ApplicationContext appContext: Context): MyDataStore {
        return MyDataStore(appContext)
    }

}