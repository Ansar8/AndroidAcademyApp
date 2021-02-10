package ru.sandbox.androidacademyapp.data.db.entities.relations

import androidx.room.ColumnInfo
import androidx.room.Entity
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.MovieCategoryCrossRefs


@Entity(
    tableName = MovieCategoryCrossRefs.TABLE_NAME,
    primaryKeys = [MovieCategoryCrossRefs.COLUMN_NAME_MOVIE_ID, MovieCategoryCrossRefs.COLUMN_NAME_CATEGORY_ID]
)
data class MovieCategoryCrossRef(
    @ColumnInfo(name = MovieCategoryCrossRefs.COLUMN_NAME_MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = MovieCategoryCrossRefs.COLUMN_NAME_CATEGORY_ID)
    val categoryId: Int
)