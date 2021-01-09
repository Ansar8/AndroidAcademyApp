package ru.sandbox.androidacademyapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        get() = if (this.adult) 16 else 13
}