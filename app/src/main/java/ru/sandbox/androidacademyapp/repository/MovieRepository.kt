package ru.sandbox.androidacademyapp.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.data.network.MoviesApi
import ru.sandbox.androidacademyapp.data.network.responses.ActorResponse
import ru.sandbox.androidacademyapp.data.network.responses.MovieResponse
import ru.sandbox.androidacademyapp.util.Response
import ru.sandbox.androidacademyapp.data.db.MoviesDao
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Category
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.CategoryWithMovies
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieCategoryCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors
import ru.sandbox.androidacademyapp.ui.movies.MoviesCategory
import kotlin.math.roundToInt

class MovieRepository(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao): IMovieRepository {

    override suspend fun getCategoryWithMovies(categoryName: String): Response<CategoryWithMovies> =
        withContext(Dispatchers.IO) {
            try {
                val category = Category(
                    MoviesCategory.valueOf(categoryName).ordinal,
                    MoviesCategory.valueOf(categoryName).name
                )

                val response = when (MoviesCategory.valueOf(categoryName)){
                    MoviesCategory.POPULAR -> moviesApi.getPopularMovies()
                    MoviesCategory.UPCOMING -> moviesApi.getUpcomingMovies()
                    MoviesCategory.TOPRATED -> moviesApi.getTopRatedMovies()
                }

                val responseWithDetails = response.movies.map { moviesApi.getMovieDetails(it.id) }
                val movies = responseWithDetails.map { toMovieEntity(it) }

                val categoryWithMovies = CategoryWithMovies(category = category, movies = movies)
                Response.Success(categoryWithMovies)
            }
            catch (t: Throwable) {
                Log.d("getMovies", t.message.toString())
                Response.Error("Oops..looks like network failure!")
            }
        }

    override suspend fun getMovieWithActors(movieId: Int): Response<MovieWithActors> =
        withContext(Dispatchers.IO) {
            try {
                val actors = moviesApi.getMovieActors(movieId).actors.take(10).map { toActorEntity(it) }
                val movie = toMovieEntity(moviesApi.getMovieDetails(movieId))
                val movieWithActors = MovieWithActors(movie, actors)

                Response.Success(movieWithActors)
            }
            catch (t: Throwable){
                Log.d("getMovieWithActors", t.message.toString())
                Response.Error("Oops..looks like network failure!")
            }
        }

    override suspend fun getSavedCategoryWithMovies(categoryName: String): List<CategoryWithMovies> =
        withContext(Dispatchers.IO){
            moviesDao.getCategoryWithMovies(categoryName)
        }

    override suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors> =
        withContext(Dispatchers.IO){
            moviesDao.getMovieWithActors(movieId)
        }

    override suspend fun saveCategoryWithMovies(categoryWithMovies: CategoryWithMovies) =
        withContext(Dispatchers.IO){
            val category = categoryWithMovies.category
            val movies = categoryWithMovies.movies

            moviesDao.insertMovies(movies)
            moviesDao.insertCategory(category)
            movies.forEach { movie ->
                val crossRef = MovieCategoryCrossRef(movie.id, category.id)
                moviesDao.insertMovieCategoryCrossRef(crossRef)
            }
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