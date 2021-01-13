package ru.sandbox.androidacademyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.repository.MovieRepository

class MoviesViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(MovieRepository(MoviesApi.create()))
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}