package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.util.Response
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

interface IMovieRepository {
    suspend fun getMovies(): Response<List<Movie>>
    suspend fun getMovieWithActors(movieId: Int): Response<MovieWithActors>
    suspend fun getSavedMovies(): List<Movie>
    suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun saveMovieWithActors(movieWithActors: MovieWithActors)
}