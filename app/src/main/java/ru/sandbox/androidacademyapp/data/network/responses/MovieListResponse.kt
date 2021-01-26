package ru.sandbox.androidacademyapp.data.network.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    @SerialName("results")
    val movies: List<MovieResponse>
)