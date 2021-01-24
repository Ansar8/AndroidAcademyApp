package ru.sandbox.androidacademyapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorListResponse(
    @SerialName("cast")
    val actors: List<ActorResponse>
)