package com.netflixclone.network.models

data class Movie(
    val id: Int,
    val image_url: String,
    val title: String,
    val category: String,
    val video_url: String
)
