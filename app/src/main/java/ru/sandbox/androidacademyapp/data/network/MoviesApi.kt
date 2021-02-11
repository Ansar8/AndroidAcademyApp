package ru.sandbox.androidacademyapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import ru.sandbox.androidacademyapp.BuildConfig
import ru.sandbox.androidacademyapp.data.network.responses.ActorListResponse
import ru.sandbox.androidacademyapp.data.network.responses.MovieListResponse
import ru.sandbox.androidacademyapp.data.network.responses.MovieResponse

interface MoviesApi {

    @GET("movie/popular?page=1")
    suspend fun getPopularMovies(): MovieListResponse

    @GET("movie/upcoming?page=1")
    suspend fun getUpcomingMovies(): MovieListResponse

    @GET("movie/top_rated?page=1")
    suspend fun getTopRatedMovies(): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movie_id: Int): MovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(@Path("movie_id") movie_id: Int): ActorListResponse

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        @Suppress("EXPERIMENTAL_API_USAGE")
        fun create(): MoviesApi {
            val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(MoviesApiHeaderInterceptor())
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(MoviesApi::class.java)
        }
    }
}