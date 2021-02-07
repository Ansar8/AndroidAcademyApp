package ru.sandbox.androidacademyapp.ui.movies

import ru.sandbox.androidacademyapp.util.SingleLiveEvent
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.util.Response
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MoviesViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: SingleLiveEvent<String> get() = _errorMessage

    private val _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> = _movieList

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = false
            _errorMessage.value = exception.message
    }

    fun loadMovies(type: String){
        viewModelScope.launch(loadingExceptionHandler) {

            _isLoading.value = true
//            val savedMovies = repository.getSavedMovies()
//            if (savedMovies.isNotEmpty()) _movieList.value = savedMovies

            when (val response = repository.getMovies(type)){
                is Response.Success -> {
                    response.data?.let { remoteMovies ->
//                        repository.saveMovies(remoteMovies)
                        _movieList.value = remoteMovies
                    }
                }
                is Response.Error -> _errorMessage.value = response.message
            }

            _isLoading.value = false
        }
    }
}