package ru.sandbox.androidacademyapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResults(
    @SerialName("results")
    val movies: List<Movie>
)