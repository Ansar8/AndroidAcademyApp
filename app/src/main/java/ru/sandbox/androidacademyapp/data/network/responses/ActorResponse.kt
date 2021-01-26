package ru.sandbox.androidacademyapp.data.network.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.BuildConfig

@Serializable
data class ActorResponse(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val picture: String?
){
    val pictureUrl: String
        get() = BuildConfig.IMAGES_BASE_URL + BuildConfig.PROFILE_SIZE + picture
}
