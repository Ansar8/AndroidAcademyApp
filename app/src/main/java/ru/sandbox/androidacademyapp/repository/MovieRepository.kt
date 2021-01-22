package ru.sandbox.androidacademyapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.MovieResponse
import ru.sandbox.androidacademyapp.api.Result
import ru.sandbox.androidacademyapp.data.db.MoviesDao
import ru.sandbox.androidacademyapp.data.db.entites.Movie

class MovieRepository(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao): IMovieRepository {

    override suspend fun getPopularMovies(): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = moviesApi.getMovies()
            val responseWithDetails = response.movies.map { moviesApi.getMovieDetails(it.id) }
            val movies = responseWithDetails.map { toEntity(it) }
            Result.Success(movies)
        }
        catch (t: Throwable) {
            Result.Error("Oops..looks like network failure!")
        }
    }

    override suspend fun savePopularMovies(movies: List<Movie>){
        moviesDao.insertMovies(movies)
    }

    override suspend fun getActors(movie_id: Int): List<ActorResponse> {
        val response = moviesApi.getMovieActors(movie_id)
        return response.actors.take(10)
    }

    override suspend fun getSavedPopularMovies(): List<Movie>{
        return moviesDao.getPopularMovies()
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