package ru.sandbox.androidacademyapp.data.db

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieActorCross

@Entity(
    tableName = MovieActorCross.TABLE_NAME,
    primaryKeys = [MovieActorCross.COLUMN_NAME_MOVIE_ID, MovieActorCross.COLUMN_NAME_ACTOR_ID]
)
data class MovieActorCrossRef(
    @ColumnInfo(name = MovieActorCross.COLUMN_NAME_MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = MovieActorCross.COLUMN_NAME_ACTOR_ID)
    val actorId: Int
)