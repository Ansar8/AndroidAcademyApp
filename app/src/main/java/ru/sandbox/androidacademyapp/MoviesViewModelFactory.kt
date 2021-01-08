package ru.sandbox.androidacademyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sandbox.androidacademyapp.repository.MovieRepositoryImpl

class MoviesViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(MovieRepositoryImpl())
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}