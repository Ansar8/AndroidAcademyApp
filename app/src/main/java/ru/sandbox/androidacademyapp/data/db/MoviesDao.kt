package ru.sandbox.androidacademyapp.data.db

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieActorCrossRefs.COLUMN_NAME_MOVIE_ID
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Categories.COLUMN_NAME_NAME
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.Category
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieCategoryCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors
import ru.sandbox.androidacademyapp.data.db.entities.relations.CategoryWithMovies

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieCategoryCrossRef(crossRef: MovieCategoryCrossRef)

    @Transaction
    @Query("SELECT * FROM Category WHERE $COLUMN_NAME_NAME == :categoryName")
    suspend fun getCategoryWithMovies(categoryName: String): List<CategoryWithMovies>

    @Transaction
    @Query("SELECT * FROM Movie WHERE $COLUMN_NAME_MOVIE_ID == :movieId")
    suspend fun getMovieWithActors(movieId: Int): List<MovieWithActors>

}