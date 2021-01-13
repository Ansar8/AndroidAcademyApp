package ru.sandbox.androidacademyapp.repository

import androidx.paging.PagingSource
import ru.sandbox.androidacademyapp.api.MoviesApi
import ru.sandbox.androidacademyapp.data.Movie
import java.io.IOException
import retrofit2.HttpException

private const val MOVIE_STARTING_PAGE_INDEX = 1

class MoviePagingSource(private val moviesApi: MoviesApi): PagingSource<Int, Movie>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: MOVIE_STARTING_PAGE_INDEX

        return try {
            val response = moviesApi.getMovies(position)
            val movies = response.movies.map { moviesApi.getMovieDetails(it.id) }
            val prevKey = if (position == MOVIE_STARTING_PAGE_INDEX) null else position - 1
            val nextKey = if (movies.isEmpty()) null else position + 1

            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}