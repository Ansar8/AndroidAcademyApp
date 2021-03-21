package ru.sandbox.androidacademyapp.ui.moviesearch

import ru.sandbox.androidacademyapp.data.db.entities.Movie

/**
 * Class containing the result of the Movie request
 */

sealed class SearchResult {
    data class Success(val movieList: List<Movie>) : SearchResult()
    data class Error(val error: Throwable) : SearchResult()
    object EmptyContent : SearchResult()
    object EmptyQuery : SearchResult()
    //sealed class HttpErrors : NetworkState() //TODO: check how it works and implement
}