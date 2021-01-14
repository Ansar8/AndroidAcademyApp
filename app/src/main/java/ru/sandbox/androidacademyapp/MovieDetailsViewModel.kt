package ru.sandbox.androidacademyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isDetailsLoaded = MutableLiveData(true)
    val isDetailsLoaded: LiveData<Boolean> = _isDetailsLoaded

    private val _isDetailsLoading = MutableLiveData(false)
    val isDetailsLoading: LiveData<Boolean> = _isDetailsLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> = _movieDetails

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isError.value = true
            _isDetailsLoading.value = false
    }

    fun getMovieDetails(movie_id: Int) {
        viewModelScope.launch(loadingExceptionHandler) {
            _isDetailsLoading.value = true
            _isDetailsLoaded.value = false
            _isError.value = false

            val movie = repository.getMovie(movie_id)
            val actors = repository.getActors(movie_id)
            movie.actors = actors

            _movieDetails.value = movie
            _isDetailsLoaded.value = true
            _isDetailsLoading.value = false
        }
    }
}