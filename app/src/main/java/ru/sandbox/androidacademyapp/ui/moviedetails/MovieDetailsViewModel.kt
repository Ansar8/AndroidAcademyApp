package ru.sandbox.androidacademyapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.LoadState
import ru.sandbox.androidacademyapp.util.SingleLiveEvent


class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<LoadState>()
    val isLoading: LiveData<LoadState> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: SingleLiveEvent<String> get() = _errorMessage

    private val _movieDetails = MutableLiveData<MovieWithActors>()
    val movieDetails: LiveData<MovieWithActors> = _movieDetails

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = LoadState.Ready
            _errorMessage.value = exception.message
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = LoadState.Loading

            val savedDetails = repository.getSavedMovieWithActors(movieId)
            if(savedDetails.isNotEmpty()) {
                val details = savedDetails.first()
                    _movieDetails.value = details
            }

            var remoteDetails: MovieWithActors? = null
            try {
                remoteDetails = repository.getMovieWithActors(movieId)
                _movieDetails.value = remoteDetails
            }
            catch (error: Throwable){
                _errorMessage.value = "Oops.. looks like network failure!!"
            }

            _isLoading.value = LoadState.Ready

            if (remoteDetails != null){
                repository.saveMovieWithActors(remoteDetails)
            }
        }
    }
}