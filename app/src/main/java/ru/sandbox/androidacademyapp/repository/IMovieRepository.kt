package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

interface IMovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getActors(movie_id: Int): List<Actor>
}