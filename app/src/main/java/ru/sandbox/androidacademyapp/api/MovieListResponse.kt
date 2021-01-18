package ru.sandbox.androidacademyapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.data.Movie

@Serializable
data class MovieListResponse(
    @SerialName("results")
    val movies: List<Movie>
)