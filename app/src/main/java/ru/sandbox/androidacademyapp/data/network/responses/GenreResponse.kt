package ru.sandbox.androidacademyapp.data.network.responses

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val id: Int, val name: String)