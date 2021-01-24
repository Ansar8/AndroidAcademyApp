package ru.sandbox.androidacademyapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.api.ActorResponse
import ru.sandbox.androidacademyapp.api.MovieResponse
import ru.sandbox.androidacademyapp.api.Result
import ru.sandbox.androidacademyapp.data.db.MoviesDao
import ru.sandbox.androidacademyapp.data.db.entites.Actor
import ru.sandbox.androidacademyapp.data.db.entites.Movie
import ru.sandbox.androidacademyapp.data.db.entites.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entites.relations.MovieWithActors
import kotlin.math.roundToInt

class MovieRepository(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao): IMovieRepository {

    override suspend fun getMovies(): Result<List<Movie>> =
        withContext(Dispatchers.IO) {
            try {
                val response = moviesApi.getMovies()
                val responseWithDetails = response.movies.map { moviesApi.getMovieDetails(it.id) }
                val movies = responseWithDetails.map { toMovieEntity(it) }
                Result.Success(movies)
            }
            catch (t: Throwable) {
                Result.Error("Oops..looks like network failure!")
            }
        }

    override suspend fun getMovieWithActors(movieId: Int): Result<MovieWithActors> =
        withContext(Dispatchers.IO) {
            try {
                val actors = moviesApi.getMovieActors(movieId).actors.map { toActorEntity(it) }
                val movie = toMovieEntity(moviesApi.getMovieDetails(movieId))
                val movieWithActors = MovieWithActors(movie, actors)

                Result.Success(movieWithActors)
            }
            catch (t: Throwable){
                Result.Error("Oops..looks like network failure!")
            }
        }

    override suspend fun getSavedMovies(): List<Movie>{
        return moviesDao.getPopularMovies()
    }

    override suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors> {
        return moviesDao.getMovieWithActors(movieId)
    }

    override suspend fun saveMovies(movies: List<Movie>){
        moviesDao.insertMovies(movies)
    }

    override suspend fun saveMovieWithActors(movieWithActors: MovieWithActors) {
        val movie = movieWithActors.movie
        val actors = movieWithActors.actors

        moviesDao.insertActors(actors)
        actors.forEach { actor ->
            val crossRef = MovieActorCrossRef(movie.id, actor.id)
            moviesDao.insertMovieActorCrossRef(crossRef)
        }
    }

    private fun toActorEntity(actor: ActorResponse) = Actor(
        id = actor.id,
        name = actor.name,
        pictureUrl = BuildConfig.IMAGES_BASE_URL + BuildConfig.PROFILE_SIZE + actor.picture
    )

    private fun toMovieEntity(movie: MovieResponse) = Movie(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        posterUrl = BuildConfig.IMAGES_BASE_URL + BuildConfig.POSTER_SIZE + movie.poster,
        backdropUrl = BuildConfig.IMAGES_BASE_URL + BuildConfig.BACKDROP_SIZE + movie.backdrop,
        ratings = movie.ratings.div(10).times(5).roundToInt(),
        reviews = movie.reviews,
        minAge = if (movie.adult) 16 else 13,
        runtime = movie.runtime,
        genres = movie.genres.joinToString { it.name }
    )

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
    }
}