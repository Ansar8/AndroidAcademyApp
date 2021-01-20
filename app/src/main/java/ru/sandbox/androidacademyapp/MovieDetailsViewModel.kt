package ru.sandbox.androidacademyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository


enum class LoadState {
    NotLoading, Loading, Error
}

class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _loadState = MutableLiveData(LoadState.NotLoading)
    val loadState: LiveData<LoadState> = _loadState

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> = _movieDetails

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _loadState.value = LoadState.Error
    }

    fun getMovieDetails(movie_id: Int) {
        viewModelScope.launch(loadingExceptionHandler) {
            _loadState.value = LoadState.Loading

            val movie = repository.getMovie(movie_id)
            val actors = repository.getActors(movie_id)
            movie.actors = actors

            _movieDetails.value = movie
            _loadState.value = LoadState.NotLoading
        }
    }
}