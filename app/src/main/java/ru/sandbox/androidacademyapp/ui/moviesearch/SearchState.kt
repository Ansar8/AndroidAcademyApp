package ru.sandbox.androidacademyapp.ui.moviesearch

sealed class SearchState {
    object Loading : SearchState()
    object Ready : SearchState()
}