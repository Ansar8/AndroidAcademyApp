package ru.sandbox.androidacademyapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sandbox.androidacademyapp.data.Actor

@Serializable
data class ActorListResponse(
    @SerialName("cast")
    val actors: List<Actor>
)