package ru.sandbox.androidacademyapp

import android.content.Context
import ru.sandbox.androidacademyapp.data.*

class MoviesLoader(private val context: Context) {
    suspend fun loadMovieList(): List<Movie> {
        return loadMovies(context)
    }
}