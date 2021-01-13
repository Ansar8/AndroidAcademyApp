package ru.sandbox.androidacademyapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.BuildConfig
import kotlin.math.roundToInt

@Serializable
data class Movie(
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
    val numberOfRatings: Int,
    val adult: Boolean,
    val runtime: Int? = null,
    val genres: List<Genre> = emptyList(),
    val actors: List<Actor> = emptyList()
){
    val minimumAge: Int
        get() = if (adult) 16 else 13

    val posterUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.POSTER_SIZE + poster

    val backdropUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.BACKDROP_SIZE + backdrop

    val ratingOutOfFive: Int
        get() = ratings.div(10).times(5).roundToInt()
}