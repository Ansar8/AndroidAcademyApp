package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.util.Result
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

interface IMovieRepository {
    suspend fun getMovies(): Result<List<Movie>>
    suspend fun getMovieWithActors(movieId: Int): Result<MovieWithActors>
    suspend fun getSavedMovies(): List<Movie>
    suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun saveMovieWithActors(movieWithActors: MovieWithActors)
}