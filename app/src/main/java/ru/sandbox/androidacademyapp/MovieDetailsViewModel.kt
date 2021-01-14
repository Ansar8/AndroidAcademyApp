package ru.sandbox.androidacademyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isActorsLoadingError = MutableLiveData(false)
    val isActorsLoadingError: LiveData<Boolean> = _isActorsLoadingError

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> = _movieDetails

    private val _actorList = MutableLiveData<List<Actor>>(emptyList())
    val actorList: LiveData<List<Actor>> = _actorList

    private val actorsLoadingExceptionHandler = CoroutineExceptionHandler {
            coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
        _isActorsLoadingError.value = true
    }

    fun loadActors(movie_id: Int){
        viewModelScope.launch(actorsLoadingExceptionHandler) {
            _isActorsLoadingError.value = false
            val loadedActors = repository.getActors(movie_id)
            _actorList.value = loadedActors
        }
    }

    fun getMovieDetails(movie_id: Int) {
        viewModelScope.launch {
            _movieDetails.value = repository.getMovieDetails(movie_id)
        }
    }
}