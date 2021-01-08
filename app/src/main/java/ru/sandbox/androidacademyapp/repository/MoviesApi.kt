package ru.sandbox.androidacademyapp.repository

import retrofit2.http.GET
import retrofit2.http.Path
import ru.sandbox.androidacademyapp.data.Movie

interface MoviesApi {
    @GET("movie/popular?page=1")
    suspend fun getMovies(): List<Movie>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movie_id: Int): Movie

//    @GET("3/movie/{movie_id}/credits")
//    suspend fun getMovieCredits(@Path("movie_id") movie_id: Int): MovieCredits
}