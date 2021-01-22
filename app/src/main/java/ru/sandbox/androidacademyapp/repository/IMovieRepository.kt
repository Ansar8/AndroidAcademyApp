package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.Result
import ru.sandbox.androidacademyapp.data.db.entites.Movie

interface IMovieRepository {
    suspend fun getPopularMovies(): Result<List<Movie>>
    suspend fun getSavedPopularMovies(): List<Movie>
    suspend fun savePopularMovies(movies: List<Movie>)
    suspend fun getActors(movie_id: Int): List<ActorResponse>
}