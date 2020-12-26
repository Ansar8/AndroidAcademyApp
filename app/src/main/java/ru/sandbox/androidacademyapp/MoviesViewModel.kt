package ru.sandbox.androidacademyapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.data.loadMovies

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val exceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    fun loadMovies(){
        viewModelScope.launch(exceptionHandler) {
            _isLoading.value = true

            Log.d("ViewModel", "Loading movies from view model...")
            val loadedMovies = loadMovies(getApplication())
            _movieList.value = loadedMovies

            _isLoading.value = false
        }
    }
}