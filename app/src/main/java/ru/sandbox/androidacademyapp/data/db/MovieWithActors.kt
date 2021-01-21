package ru.sandbox.androidacademyapp.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Actors
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieActorCross
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Movies

data class MovieWithActors(
    @Embedded
    val movie: Movie,
    @Relation(
        parentColumn = Movies.COLUMN_NAME_ID,
        entityColumn = Actors.COLUMN_NAME_ID,
        associateBy = Junction(MovieActorCross::class)
    )
    val actors: List<Actor>
)