package ru.sandbox.androidacademyapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.LoadState
import ru.sandbox.androidacademyapp.util.SingleLiveEvent

class MoviesViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<LoadState>()
    val isLoading: LiveData<LoadState> = _isLoading

    private val _toastMessage = SingleLiveEvent<String>()
    val errorMessage: SingleLiveEvent<String> get() = _toastMessage

    private val _moviesResult = MutableLiveData<MoviesResult>()
    val moviesResult: LiveData<MoviesResult> = _moviesResult

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = LoadState.Ready
            _toastMessage.value = "Oops.. something unexpected happened !!"
            _moviesResult.value = MoviesResult.Error(exception)
    }

    init { loadMovies() }

    fun loadMovies(){
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = LoadState.Loading

            //show cached movies from start
            val savedMovies = repository.getSavedMovies()
            if (savedMovies.isNotEmpty())
                _moviesResult.value = MoviesResult.Success(savedMovies)

            //make an update from network
            try {
                val remoteMovies = repository.getMovies()

                //show updated movie list
                if (remoteMovies.isNotEmpty()) {
                    repository.saveMovies(remoteMovies)
                    _moviesResult.value = MoviesResult.Success(repository.getSavedMovies())
                }

                //notify that no movies were found
                if (remoteMovies.isEmpty() && savedMovies.isEmpty()) {
                    _toastMessage.value = "Oops.. no movies were found."
                    _moviesResult.value = MoviesResult.EmptyContent
                }
            }
            catch (error: Throwable){
                if (savedMovies.isNotEmpty()){
                    //notify to check connection and show cached movies
                    _moviesResult.value = MoviesResult.ErrorWithCache(savedMovies, error)
                }
                else {
                    //notify to check connection and show message to refresh the page
                    _moviesResult.value = MoviesResult.Error(error)
                }
                _toastMessage.value = "Oops.. looks like network failure!"
            }

            _isLoading.value = LoadState.Ready
        }
    }
}