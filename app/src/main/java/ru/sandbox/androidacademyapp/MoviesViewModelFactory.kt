package ru.sandbox.androidacademyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(MovieRepositoryImpl(context))
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}