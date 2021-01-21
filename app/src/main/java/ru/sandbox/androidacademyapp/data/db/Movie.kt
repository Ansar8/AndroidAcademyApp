package ru.sandbox.androidacademyapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Movies

@Entity(tableName = Movies.TABLE_NAME)
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = Movies.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = Movies.COLUMN_NAME_TITLE)
    val title: String,

    @ColumnInfo(name = Movies.COLUMN_NAME_OVERVIEW)
    val overview: String?,

    @ColumnInfo(name = Movies.COLUMN_NAME_POSTER)
    val poster: String?,

    @ColumnInfo(name = Movies.COLUMN_NAME_BACKDROP)
    val backdrop: String?,

    @ColumnInfo(name = Movies.COLUMN_NAME_RATINGS)
    val ratings: Float,

    @ColumnInfo(name = Movies.COLUMN_NAME_REVIEWS)
    val reviews: Int,

    @ColumnInfo(name = Movies.COLUMN_NAME_ADULT)
    val adult: Boolean,

    @ColumnInfo(name = Movies.COLUMN_NAME_RUNTIME)
    val runtime: Int? = null,

    @ColumnInfo(name = Movies.COLUMN_NAME_GENRES)
    val genres: String
)