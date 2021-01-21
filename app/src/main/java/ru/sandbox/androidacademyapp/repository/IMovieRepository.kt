package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.MovieResponse

interface IMovieRepository {
    suspend fun getMovies(): List<MovieResponse>
    suspend fun getActors(movie_id: Int): List<ActorResponse>
}