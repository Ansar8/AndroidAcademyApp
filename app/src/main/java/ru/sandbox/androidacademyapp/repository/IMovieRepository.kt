package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.data.Movie

interface IMovieRepository {
    suspend fun getMovies(): List<Movie>
}