package ru.sandbox.androidacademyapp.data.db

import android.provider.BaseColumns

object MoviesDbContract {

    const val DATABASE_NAME = "Movies.db"

    object Movies{
        const val TABLE_NAME = "movies"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_POSTER = "poster"
        const val COLUMN_NAME_BACKDROP = "backdrop"
        const val COLUMN_NAME_RATINGS = "ratings"
        const val COLUMN_NAME_REVIEWS = "reviews"
        const val COLUMN_NAME_ADULT = "adult"
        const val COLUMN_NAME_RUNTIME = "runtime"
        const val COLUMN_NAME_GENRES = "genres"
    }

    object Actors{
        const val TABLE_NAME = "actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "title"
        const val COLUMN_NAME_PICTURE = "picture"
    }

    object MovieActorCross{
        const val TABLE_NAME = "movieactorcross"

        const val COLUMN_NAME_MOVIE_ID = "movieId"
        const val COLUMN_NAME_ACTOR_ID = "actorId"
    }

}