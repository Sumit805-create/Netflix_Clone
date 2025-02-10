package com.netflixclone.network.models

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object RetrofitClient {

    private const val BASE_URL = "http://10.0.0.2:3000/"  // Correct localhost for Android Emulator
// Replace with your actual backend IP


    private val client = OkHttpClient.Builder()
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
