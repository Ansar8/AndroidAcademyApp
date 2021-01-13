package ru.sandbox.androidacademyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

class MovieRepository(private val moviesApi: MoviesApi): IMovieRepository {

    override fun getMovies(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize =  100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(moviesApi) }
        ).liveData
    }

    override suspend fun getMovieDetails(movie_id: Int): Movie {
        return moviesApi.getMovieDetails(movie_id)
    }

    override suspend fun getActors(movie_id: Int): List<Actor> {
        val response = moviesApi.getMovieActors(movie_id)
        return response.actors.take(10)
    }

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
        private const val NETWORK_PAGE_SIZE = 20
    }
}