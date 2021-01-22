package ru.sandbox.androidacademyapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.GenreResponse

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerialName("poster_path")
    val poster: String?,
    @SerialName("backdrop_path")
    val backdrop: String?,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val reviews: Int,
    val adult: Boolean,
    val runtime: Int?,
    val genres: List<GenreResponse> = emptyList()
)