package ru.sandbox.androidacademyapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorListResponse(
    @SerialName("cast")
    val actors: List<ActorResponse>
)