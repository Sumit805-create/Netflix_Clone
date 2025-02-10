package com.netflixclone.network.models

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("signup")
    fun signup(@Body user: User): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("movies")
    fun getMovies(): Call<List<Movie>>

    @GET("movies/category")
    fun getMoviesByCategory(@Query("category") category: String): Call<List<Movie>>

    @GET("movies/list")
    fun getMovieCategories(): Call<List<String>>

    //  Send username & paymentId as a Map in the request body
    @POST("verify-payment")
    fun updateSubscription(@Body paymentData: Map<String, String>): Call<AuthResponse>
}
