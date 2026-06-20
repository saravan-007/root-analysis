package com.example.rootanalysis.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var currentBaseUrl = "http://10.0.2.2:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private var retrofit: Retrofit = buildRetrofit()

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)

    fun updateBaseUrl(newUrl: String) {
        if (newUrl.isNotEmpty() && newUrl != currentBaseUrl) {
            currentBaseUrl = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
            retrofit = buildRetrofit()
        }
    }

    fun getBaseUrl(): String = currentBaseUrl

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(currentBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
