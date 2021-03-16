package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

interface IMovieRepository {
    suspend fun searchMovies(query: String, page: Int): List<Movie>
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieWithActors(movieId: Int): MovieWithActors
    suspend fun getSavedMovies(): List<Movie>
    suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun saveMovieWithActors(movieWithActors: MovieWithActors)
}