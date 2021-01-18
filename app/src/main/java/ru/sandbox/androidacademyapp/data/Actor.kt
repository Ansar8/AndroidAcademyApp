package ru.sandbox.androidacademyapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.BuildConfig

@Serializable
data class Actor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val picture: String?
){
    val pictureUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.PROFILE_SIZE + picture
}
