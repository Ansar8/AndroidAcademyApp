package ru.sandbox.androidacademyapp

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MoviesViewModel(private val repository: IMovieRepository) : ViewModel() {
    val movies: LiveData<PagingData<Movie>> = repository.getPopularMovies().cachedIn(viewModelScope)
}