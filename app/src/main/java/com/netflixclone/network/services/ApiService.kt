package com.netflixclone.network.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data classes for API requests & responses
data class LoginRequest(val username: String, val password: String)
data class TokenResponse(val token: String)

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("user/generateToken")
    fun login(@Body request: LoginRequest): Call<TokenResponse>

    @Headers("Content-Type: application/json")
    @POST("user/validateToken")
    fun validateToken(@Body token: String): Call<Boolean>
}
