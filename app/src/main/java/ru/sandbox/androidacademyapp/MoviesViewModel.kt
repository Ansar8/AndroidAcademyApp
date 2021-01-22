package ru.sandbox.androidacademyapp

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.Result
import ru.sandbox.androidacademyapp.data.db.entites.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MoviesViewModel(private val repository: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isActorsLoadingError = MutableLiveData(false)
    val isActorsLoadingError: LiveData<Boolean> = _isActorsLoadingError

    private val _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> = _movieList

    private val _actorList = MutableLiveData<List<ActorResponse>>(emptyList())
    val actorList: LiveData<List<ActorResponse>> = _actorList

    private val actorsLoadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isActorsLoadingError.value = true
    }

    private val loadingExceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
            println("CoroutineExceptionHandler got $exception in $coroutineContext")
            _isLoading.value = false
            _errorMessage.value = exception.message
    }

    fun loadMovies(){
        viewModelScope.launch(loadingExceptionHandler) {
            _isLoading.value = true
            val cachedMovies = repository.getSavedPopularMovies()
            if (cachedMovies.isNotEmpty()) _movieList.value = cachedMovies

            when (val result = repository.getPopularMovies()){
                is Result.Success -> {
                    result.data?.let { remoteMovies ->
                        repository.savePopularMovies(remoteMovies)
                        _movieList.value = remoteMovies
                    }
                }
                is Result.Error -> _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun loadActors(movie_id: Int){
        viewModelScope.launch(actorsLoadingExceptionHandler) {
            _isActorsLoadingError.value = false
            val loadedActors = repository.getActors(movie_id)
            _actorList.value = loadedActors
        }
    }

    fun getMovieById(id: Int?): Movie? {
        return movieList.value?.find { it.id == id }
    }
}