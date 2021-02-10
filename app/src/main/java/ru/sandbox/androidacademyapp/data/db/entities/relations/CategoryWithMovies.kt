package ru.sandbox.androidacademyapp.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Movies
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Categories
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.data.db.entities.Category

data class CategoryWithMovies(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = Categories.COLUMN_NAME_ID,
        entityColumn = Movies.COLUMN_NAME_ID,
        associateBy = Junction(MovieCategoryCrossRef::class)
    )
    val movies: List<Movie>
)