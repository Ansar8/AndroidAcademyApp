package ru.sandbox.androidacademyapp.data.db.entites.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Actors
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Movies
import ru.sandbox.androidacademyapp.data.db.entites.Actor
import ru.sandbox.androidacademyapp.data.db.entites.Movie

data class MovieWithActors(
    @Embedded
    val movie: Movie,
    @Relation(
        parentColumn = Movies.COLUMN_NAME_ID,
        entityColumn = Actors.COLUMN_NAME_ID,
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val actors: List<Actor>
)