package ru.sandbox.androidacademyapp.data.api

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val id: Int, val name: String)