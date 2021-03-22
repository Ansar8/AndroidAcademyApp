package ru.sandbox.androidacademyapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.LoadState
import ru.sandbox.androidacademyapp.util.SingleLiveEvent


class MovieDetailsViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<LoadState>()
    val isLoading: LiveData<LoadState> = _isLoading

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: SingleLiveEvent<String> get() = _toastMessage

    private val _detailsResult = MutableLiveData<DetailsResult>()
    val detailsResult: LiveData<DetailsResult> = _detailsResult

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = LoadState.Ready
            _detailsResult.value = DetailsResult.Error(exception)
            _toastMessage.value = "Oops.. something unexpected happened !!"

    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = LoadState.Loading
            var loadingError: Throwable? = null

            // get remote details
            try {
                val remoteDetails = repository.getMovieWithActors(movieId)
                // save remote details
                repository.saveMovieWithActors(remoteDetails)
            }
            catch (error: Throwable){
                loadingError = error
            }

            // use the database as a single source of truth
            val savedDetails = repository.getSavedMovieWithActors(movieId)
            val details = savedDetails.first()


            if (loadingError != null){
                //checking if there are full movie's details
                if (details.actors.isNotEmpty())
                    _detailsResult.value = DetailsResult.ErrorWithCache(details, loadingError)
                else
                    _detailsResult.value = DetailsResult.Error(loadingError)

                _toastMessage.value = "Oops.. looks like network failure!!"
            }
            else {
                _detailsResult.value = DetailsResult.Success(details)
            }

            _isLoading.value = LoadState.Ready
        }
    }
}