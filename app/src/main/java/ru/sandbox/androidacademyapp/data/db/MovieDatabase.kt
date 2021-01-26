package ru.sandbox.androidacademyapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef

@Database(
    entities = [
        Movie::class,
        Actor::class,
        MovieActorCrossRef::class
    ],
    version = 2
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val moviesDao: MoviesDao

    companion object {

        fun create(applicationContext: Context): MovieDatabase =
            Room.databaseBuilder(
                applicationContext,
                MovieDatabase::class.java,
                MoviesDbContract.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}