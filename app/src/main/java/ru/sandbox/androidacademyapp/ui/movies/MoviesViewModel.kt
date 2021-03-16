package ru.sandbox.androidacademyapp.ui.movies

import ru.sandbox.androidacademyapp.util.SingleLiveEvent
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.LoadState

class MoviesViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<LoadState>()
    val isLoading: LiveData<LoadState> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: SingleLiveEvent<String> get() = _errorMessage

    private val _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> = _movieList

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = LoadState.Ready
            _errorMessage.value = exception.message
    }

    fun loadMovies(){
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = LoadState.Loading

            val savedMovies = repository.getSavedMovies()
            if (savedMovies.isNotEmpty()) _movieList.value = savedMovies

            var remoteMovies = emptyList<Movie>()

            try {
                remoteMovies = repository.getMovies()
                if (remoteMovies.isNotEmpty())
                    _movieList.value = remoteMovies
                else if (savedMovies.isEmpty())
                    _errorMessage.value = "Oops.. no movies were found."
            }
            catch (error: Throwable){
                _errorMessage.value = "Oops.. looks like network failure!"
            }

            _isLoading.value = LoadState.Ready

            if (remoteMovies.isNotEmpty())
                repository.saveMovies(remoteMovies)

        }
    }
}