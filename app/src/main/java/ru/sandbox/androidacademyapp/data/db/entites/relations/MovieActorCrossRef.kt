package ru.sandbox.androidacademyapp.data.db.entites.relations

import androidx.room.*
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieActorCrossRefs

@Entity(
    tableName = MovieActorCrossRefs.TABLE_NAME,
    primaryKeys = [MovieActorCrossRefs.COLUMN_NAME_MOVIE_ID, MovieActorCrossRefs.COLUMN_NAME_ACTOR_ID]
)
data class MovieActorCrossRef(
    @ColumnInfo(name = MovieActorCrossRefs.COLUMN_NAME_MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = MovieActorCrossRefs.COLUMN_NAME_ACTOR_ID)
    val actorId: Int
)