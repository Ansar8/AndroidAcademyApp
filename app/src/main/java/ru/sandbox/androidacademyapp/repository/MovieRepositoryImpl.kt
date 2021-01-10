package ru.sandbox.androidacademyapp.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.data.*

class MovieRepositoryImpl(): IMovieRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(MoviesApiHeaderInterceptor())
        .build()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val moviesApi: MoviesApi = retrofit.create(MoviesApi::class.java)

    override suspend fun getMovies(): List<Movie> {
        val results = moviesApi.getMovies()
        return results.movies.map { moviesApi.getMovieDetails(it.id) }
    }

    override suspend fun getActors(movie_id: Int): List<Actor> {
        return moviesApi.getMovieActors(movie_id).actors.take(10)
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
    }
}