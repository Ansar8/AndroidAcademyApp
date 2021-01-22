package ru.sandbox.androidacademyapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.MovieResponse
import ru.sandbox.androidacademyapp.data.db.MoviesDao
import ru.sandbox.androidacademyapp.data.db.entites.Movie

class MovieRepository(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao): IMovieRepository {

    override suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val response = moviesApi.getMovies()
        val responseWithDetails = response.movies.map { moviesApi.getMovieDetails(it.id) }
        responseWithDetails.map { toEntity(it) }
    }

    override suspend fun getActors(movie_id: Int): List<ActorResponse> {
        val response = moviesApi.getMovieActors(movie_id)
        return response.actors.take(10)
    }

    private fun toEntity(movie: MovieResponse) = Movie(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        posterUrl = BuildConfig.IMAGES_BASE_URL + BuildConfig.POSTER_SIZE + movie.poster,
        backdropUrl = BuildConfig.IMAGES_BASE_URL + BuildConfig.BACKDROP_SIZE + movie.backdrop,
        ratings = movie.ratings,
        reviews = movie.reviews,
        minAge = if (movie.adult) 16 else 13,
        runtime = movie.runtime,
        genres = movie.genres.joinToString { it.name }
    )

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
    }
}