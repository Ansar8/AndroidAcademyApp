package ru.sandbox.androidacademyapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.sandbox.androidacademyapp.data.db.entities.Actor
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.Category
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieActorCrossRef
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieCategoryCrossRef

@Database(
    entities = [
        Movie::class,
        Actor::class,
        Category::class,
        MovieCategoryCrossRef::class,
        MovieActorCrossRef::class
    ],
    version = 3
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