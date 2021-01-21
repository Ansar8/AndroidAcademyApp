package ru.sandbox.androidacademyapp.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val id: Int, val name: String)