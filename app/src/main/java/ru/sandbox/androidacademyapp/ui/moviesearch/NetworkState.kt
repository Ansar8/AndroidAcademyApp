package ru.sandbox.androidacademyapp.ui.moviesearch

import ru.sandbox.androidacademyapp.data.db.entities.Movie

/**
 * Class containing the result of the Movie request
 */

sealed class NetworkState {
    data class Success(val movieList: List<Movie>) : NetworkState()
    data class Error(val error: Throwable) : NetworkState()
    object EmptyContent : NetworkState()
    object EmptyQuery : NetworkState()
    //sealed class HttpErrors : NetworkState() //TODO: check how it works and implement
}