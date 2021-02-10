package ru.sandbox.androidacademyapp.repository

import ru.sandbox.androidacademyapp.util.Response
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.CategoryWithMovies
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

interface IMovieRepository {
    suspend fun getCategoryWithMovies(categoryName: String): Response<CategoryWithMovies>
    suspend fun getMovieWithActors(movieId: Int): Response<MovieWithActors>
    suspend fun getSavedCategoryWithMovies(categoryName: String): List<CategoryWithMovies>
    suspend fun getSavedMovieWithActors(movieId: Int): List<MovieWithActors>
    suspend fun saveCategoryWithMovies(categoryWithMovies: CategoryWithMovies)
    suspend fun saveMovieWithActors(movieWithActors: MovieWithActors)
}