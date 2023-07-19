package com.kartheek.weatherapp

import android.content.Context
import com.kartheek.weatherapp.common.isNetworkAvailable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ForceCacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!isNetworkAvailable(context)) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }
}


