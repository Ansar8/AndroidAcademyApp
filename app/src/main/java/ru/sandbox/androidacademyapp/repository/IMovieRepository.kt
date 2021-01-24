package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.Result
import ru.sandbox.androidacademyapp.data.db.entites.Actor
import ru.sandbox.androidacademyapp.data.db.entites.Movie
import ru.sandbox.androidacademyapp.data.db.entites.relations.MovieWithActors

interface IMovieRepository {
    suspend fun getMovies(): Result<List<Movie>>
    suspend fun getMovieWithActors(movieId: Int): Result<MovieWithActors>
    suspend fun getSavedMovies(): List<Movie>
    suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun saveMovieWithActors(movieWithActors: MovieWithActors)
}