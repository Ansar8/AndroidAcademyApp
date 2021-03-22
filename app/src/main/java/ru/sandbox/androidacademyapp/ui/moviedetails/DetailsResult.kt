package ru.sandbox.androidacademyapp.ui.moviedetails

import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

sealed class DetailsResult {
    data class Success(val movieDetails: MovieWithActors): DetailsResult()
    data class Error(val error: Throwable): DetailsResult()
    data class ErrorWithCache(val cachedMovieDetails: MovieWithActors, val error: Throwable): DetailsResult()
}