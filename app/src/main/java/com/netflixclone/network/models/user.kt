package com.netflixclone.network.models

data class User(
    val username: String,
    val password: String
)

data class AuthResponse(
    val message: String
)
