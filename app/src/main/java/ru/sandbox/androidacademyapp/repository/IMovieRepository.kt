package ru.sandbox.androidacademyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

interface IMovieRepository {
    fun getPopularMovies(): LiveData<PagingData<Movie>>
    suspend fun getMovie(movie_id: Int): Movie
    suspend fun getActors(movie_id: Int): List<Actor>
}