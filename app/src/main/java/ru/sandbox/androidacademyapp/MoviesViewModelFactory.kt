package ru.sandbox.androidacademyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sandbox.androidacademyapp.data.api.MoviesApi
import ru.sandbox.androidacademyapp.data.db.MovieDatabase
import ru.sandbox.androidacademyapp.repository.MovieRepository

class MoviesViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java ->
            MoviesViewModel(
                MovieRepository(
                    MoviesApi.create(),
                    MovieDatabase.create(applicationContext).moviesDao
                )
            )
        MovieDetailsViewModel::class.java ->
            MovieDetailsViewModel(
                MovieRepository(
                    MoviesApi.create(),
                    MovieDatabase.create(applicationContext).moviesDao
                )
            )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}