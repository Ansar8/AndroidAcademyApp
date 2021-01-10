package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

class MovieRepositoryImpl(): IMovieRepository {

    private val moviesApi: MoviesApi = MoviesApi.create()

    override suspend fun getMovies(): List<Movie> {
        val response = moviesApi.getMovies()
        return response.movies.map { moviesApi.getMovieDetails(it.id) }
    }

    override suspend fun getActors(movie_id: Int): List<Actor> {
        val response = moviesApi.getMovieActors(movie_id)
        return response.actors.take(10)
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
    }
}