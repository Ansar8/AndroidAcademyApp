package ru.sandbox.androidacademyapp.data.db.entites.relations

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieActorCrossRef

@Entity(
    tableName = MovieActorCrossRef.TABLE_NAME,
    primaryKeys = [MovieActorCrossRef.COLUMN_NAME_MOVIE_ID, MovieActorCrossRef.COLUMN_NAME_ACTOR_ID]
)
data class MovieActorCrossRef(
    @ColumnInfo(name = MovieActorCrossRef.COLUMN_NAME_MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = MovieActorCrossRef.COLUMN_NAME_ACTOR_ID)
    val actorId: Int
)