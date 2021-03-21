package ru.sandbox.androidacademyapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.data.network.MoviesApi
import ru.sandbox.androidacademyapp.data.network.responses.ActorResponse
import ru.sandbox.androidacademyapp.data.network.responses.MovieResponse
import ru.sandbox.androidacademyapp.data.db.MoviesDao
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors
import ru.sandbox.androidacademyapp.notifications.Notifications

class MovieRepository(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
    private val notifications: Notifications): IMovieRepository {

    override suspend fun searchMovies(query: String, page: Int): List<Movie> =
        withContext(Dispatchers.IO) {
            val response = moviesApi.searchMovie(query, page)
            response.movies
                .map { moviesApi.getMovieDetails(it.id) }
                .map(::toMovieEntity)
        }

    override suspend fun getMovies(): List<Movie> =
        withContext(Dispatchers.IO) {
            val response = moviesApi.getMovies()
            response.movies
                .map { moviesApi.getMovieDetails(it.id) }
                .map(::toMovieEntity)
        }

    override suspend fun getMovieWithActors(movieId: Int): MovieWithActors =
        withContext(Dispatchers.IO) {
            val actors = moviesApi.getMovieActors(movieId).actors.take(10).map(::toActorEntity)
            val movie = toMovieEntity(moviesApi.getMovieDetails(movieId))
            MovieWithActors(movie, actors)
        }

    override suspend fun getSavedMovies(): List<Movie> =
        withContext(Dispatchers.IO){
            moviesDao.getMovies()
        }

    override suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors> =
        withContext(Dispatchers.IO){
            notifications.dismissNotification(movieId)
            moviesDao.getMovieWithActors(movieId)
        }

    override suspend fun saveMovies(movies: List<Movie>) =
        withContext(Dispatchers.IO){
            notifyAboutNewMovie(movies)
            moviesDao.insertMovies(movies)
        }

    override suspend fun saveMovieWithActors(movieWithActors: MovieWithActors) =
        withContext(Dispatchers.IO){
            val movie = movieWithActors.movie
            val actors = movieWithActors.actors

            moviesDao.insertActors(actors)
            actors.forEach { actor ->
                val crossRef = MovieActorCrossRef(movie.id, actor.id)
                moviesDao.insertMovieActorCrossRef(crossRef)
            }
        }

    private suspend fun notifyAboutNewMovie(movies: List<Movie>) {
        val remoteMovieIds = movies.map { it.id }
        val cachedMovieIds = moviesDao.getMovies().map { it.id }
        val newMovieIds = remoteMovieIds.filter { !cachedMovieIds.contains(it) }

        val movieWithHighestRating = movies
            .filter{ movie -> movie.id in newMovieIds }
            .maxByOrNull { movie -> movie.ratings }

        if (movieWithHighestRating != null)
            notifications.showNotification(movieWithHighestRating)
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
        ratings = movie.ratings.div(10).times(5),
        reviews = movie.reviews,
        minAge = if (movie.adult) 16 else 13,
        runtime = movie.runtime,
        genres = movie.genres.joinToString { it.name }
    )

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
    }
}