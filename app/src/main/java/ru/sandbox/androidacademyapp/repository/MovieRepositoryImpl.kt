package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.MovieResponse

class MovieRepositoryImpl(private val moviesApi: MoviesApi): IMovieRepository {

    override suspend fun getMovies(): List<MovieResponse> {
        val response = moviesApi.getMovies()
        return response.movies.map { moviesApi.getMovieDetails(it.id) }
    }

    override suspend fun getActors(movie_id: Int): List<ActorResponse> {
        val response = moviesApi.getMovieActors(movie_id)
        return response.actors.take(10)
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
    }
}