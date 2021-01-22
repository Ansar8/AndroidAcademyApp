package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.data.db.entites.Movie

interface IMovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getActors(movie_id: Int): List<ActorResponse>
}