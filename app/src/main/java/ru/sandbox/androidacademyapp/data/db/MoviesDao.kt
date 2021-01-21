package ru.sandbox.androidacademyapp.data.db

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.entites.Actor
import ru.sandbox.androidacademyapp.data.db.entites.Movie
import ru.sandbox.androidacademyapp.data.db.entites.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entites.relations.MovieWithActors

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    suspend fun getPopularMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(vararg actors: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    @Transaction
    @Query("SELECT * FROM movies WHERE _id == :movieId")
    suspend fun getMovieWithActors(movieId: Int): List<MovieWithActors>

}