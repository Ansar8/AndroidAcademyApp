package ru.sandbox.androidacademyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sandbox.androidacademyapp.data.network.MoviesApi
import ru.sandbox.androidacademyapp.data.db.MovieDatabase
import ru.sandbox.androidacademyapp.notifications.NewMovieNotifications
import ru.sandbox.androidacademyapp.repository.MovieRepository
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsViewModel
import ru.sandbox.androidacademyapp.ui.movies.MoviesViewModel
import ru.sandbox.androidacademyapp.ui.moviesearch.MovieSearchViewModel

class MoviesViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val repository = MovieRepository(
            moviesApi = MoviesApi.create(),
            moviesDao = MovieDatabase.create(applicationContext).moviesDao,
            notifications = NewMovieNotifications(applicationContext)
        )
        return when (modelClass) {
            MoviesViewModel::class.java -> MoviesViewModel(repository)
            MovieDetailsViewModel::class.java -> MovieDetailsViewModel(repository)
            MovieSearchViewModel::class.java -> MovieSearchViewModel(repository)
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T
    }
}