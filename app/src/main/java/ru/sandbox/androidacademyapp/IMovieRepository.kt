package ru.sandbox.androidacademyapp

import ru.sandbox.androidacademyapp.data.Movie

interface IMovieRepository {
    suspend fun getMovies(): List<Movie>
}