package ru.sandbox.androidacademyapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class Genre(val id: Int, val name: String)