package ru.sandbox.androidacademyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

interface IMovieRepository {
    fun getMovies(): LiveData<PagingData<Movie>>
    suspend fun getMovieDetails(movie_id: Int): Movie
    suspend fun getActors(movie_id: Int): List<Actor>
}