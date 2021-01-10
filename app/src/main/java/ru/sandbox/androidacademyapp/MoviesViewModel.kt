package ru.sandbox.androidacademyapp

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MoviesViewModel(private val loader: IMovieRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> = _movieList

    private val _actorList = MutableLiveData<List<Actor>>(emptyList())
    val actorList: LiveData<List<Actor>> = _actorList

    private val exceptionHandler = CoroutineExceptionHandler {
        coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    fun loadMovies(){
        viewModelScope.launch(exceptionHandler) {
            _isLoading.value = true

            val loadedMovies = loader.getMovies()
            _movieList.value = loadedMovies

            _isLoading.value = false
        }
    }

    fun loadActors(movie_id: Int){
        viewModelScope.launch(exceptionHandler) {
            val loadedActors = loader.getActors(movie_id)
            _actorList.value = loadedActors
        }
    }

    fun getMovieById(id: Int?): Movie? {
        return movieList.value?.find { it.id == id }
    }
}