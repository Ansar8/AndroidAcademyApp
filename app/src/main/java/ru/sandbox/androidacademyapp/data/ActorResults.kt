package ru.sandbox.androidacademyapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorResults(
    @SerialName("cast")
    val actors: List<Actor>
)