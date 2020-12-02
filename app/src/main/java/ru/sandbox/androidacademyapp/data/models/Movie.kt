package ru.sandbox.androidacademyapp.data.models

data class Movie(
    val image: Int,
    val name: String,
    val hasLike: Boolean,
    val ageLimits: Int,
    val genre: String,
    val rating: Int,
    val reviews: Int,
    val duration: Int
)