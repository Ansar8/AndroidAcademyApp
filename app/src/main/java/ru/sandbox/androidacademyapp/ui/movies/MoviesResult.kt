package ru.sandbox.androidacademyapp.ui.movies

import ru.sandbox.androidacademyapp.data.db.entities.Movie

sealed class MoviesResult {
    data class Success(val movieList: List<Movie>) : MoviesResult()
    data class Error(val error: Throwable) : MoviesResult()
    data class ErrorWithCache(val error: Throwable) : MoviesResult()
    object EmptyContent : MoviesResult()
}