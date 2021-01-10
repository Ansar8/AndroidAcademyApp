package ru.sandbox.androidacademyapp.repository

import retrofit2.http.GET
import retrofit2.http.Path
import ru.sandbox.androidacademyapp.data.ActorResults
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.data.MovieResults

interface MoviesApi {

    @GET("movie/popular?page=1")
    suspend fun getMovies(): MovieResults

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movie_id: Int): Movie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(@Path("movie_id") movie_id: Int): ActorResults
}