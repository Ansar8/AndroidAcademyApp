package ru.sandbox.androidacademyapp.data.db

import android.provider.BaseColumns

object MoviesDbContract {

    const val DATABASE_NAME = "Movies.db"

    object Movies{
        const val TABLE_NAME = "Movies"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_POSTER_URL = "posterUrl"
        const val COLUMN_NAME_BACKDROP_URL = "backdropUrl"
        const val COLUMN_NAME_RATINGS = "ratings"
        const val COLUMN_NAME_REVIEWS = "reviews"
        const val COLUMN_NAME_MIN_AGE = "minAge"
        const val COLUMN_NAME_RUNTIME = "runtime"
        const val COLUMN_NAME_GENRES = "genres"
    }

    object Actors{
        const val TABLE_NAME = "Actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "title"
        const val COLUMN_NAME_PICTURE = "picture"
    }

    object MovieActorCrossRef{
        const val TABLE_NAME = "MovieWithActor"

        const val COLUMN_NAME_MOVIE_ID = "movieId"
        const val COLUMN_NAME_ACTOR_ID = "actorId"
    }

}