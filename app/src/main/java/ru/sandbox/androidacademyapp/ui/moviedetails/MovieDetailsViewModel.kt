package ru.sandbox.androidacademyapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.util.Response
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.SingleLiveEvent


class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: SingleLiveEvent<String> get() = _errorMessage

    private val _movieDetails = MutableLiveData<MovieWithActors>()
    val movieDetails: LiveData<MovieWithActors> = _movieDetails

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = false
            _errorMessage.value = exception.message
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = true
            val savedDetails = repository.getSavedMovieWithActors(movieId)
            if(savedDetails.isNotEmpty()) {
                val details = savedDetails.first()
                if (details.actors.isNotEmpty()) {
                    _movieDetails.value = details
                    _isLoading.value = false
                }
            }

            when (val response = repository.getMovieWithActors(movieId)){
                is Response.Success -> {
                    response.data?.let { movieWithActors ->
                        repository.saveMovieWithActors(movieWithActors)
                        _movieDetails.value = movieWithActors
                    }
                    _isLoading.value = false
                }
                is Response.Error -> {
                    if (_isLoading.value != null && _isLoading.value == true)
                    _errorMessage.value = response.message
                }
            }
        }
    }
}