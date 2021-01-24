package ru.sandbox.androidacademyapp.data.network

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val id: Int, val name: String)