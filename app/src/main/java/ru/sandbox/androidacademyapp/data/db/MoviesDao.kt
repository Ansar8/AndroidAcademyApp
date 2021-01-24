package ru.sandbox.androidacademyapp.data.db

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

@Dao
interface MoviesDao {

    @Query("SELECT * FROM Movies")
    suspend fun getPopularMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    @Transaction
    @Query("SELECT * FROM movies WHERE movieId == :movieId")
    suspend fun getMovieWithActors(movieId: Int): List<MovieWithActors>

}