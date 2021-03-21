package ru.sandbox.androidacademyapp.util

sealed class LoadState {
    object Loading : LoadState()
    object Ready : LoadState()
}