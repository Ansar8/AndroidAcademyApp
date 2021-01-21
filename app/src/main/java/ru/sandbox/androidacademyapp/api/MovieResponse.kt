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
    val runtime: Int? = null,
    val genres: List<GenreResponse> = emptyList(),
    val actors: List<ActorResponse> = emptyList()
){
    val minimumAge: Int
        get() = if (adult) 16 else 13

    val posterUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.POSTER_SIZE + poster

    val backdropUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.BACKDROP_SIZE + backdrop
}