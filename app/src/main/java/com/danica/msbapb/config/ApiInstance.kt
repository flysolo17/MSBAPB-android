package com.danica.msbapb.config

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {
    private const val BASE_URL = "http://192.168.100.18/msbapb/api/"

    val api: Retrofit by lazy {
        createRetrofitInstance()
    }
    private val builder = OkHttpClient.Builder().also { client ->
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(logging)

    }.build()
    private fun createRetrofitInstance(): Retrofit {
        try {
            val gson = GsonBuilder().setLenient().create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder)
                .build()
        } catch (e: Exception) {
            Log.d("API",e.message.toString(),e)
            throw RuntimeException("Retrofit initialization error", e)
        }
    }
}