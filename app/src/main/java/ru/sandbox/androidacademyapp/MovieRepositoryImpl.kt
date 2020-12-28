package ru.sandbox.androidacademyapp

import android.content.Context
import ru.sandbox.androidacademyapp.data.*

class MovieRepositoryImpl(private val context: Context): IMovieRepository {

    override suspend fun getMovies(): List<Movie> {
        return loadMovies(context)
    }
}