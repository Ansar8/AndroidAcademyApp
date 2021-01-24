package ru.sandbox.androidacademyapp.data.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sandbox.androidacademyapp.BuildConfig
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

    @ColumnInfo(name = Movies.COLUMN_NAME_POSTER_URL)
    val posterUrl: String,

    @ColumnInfo(name = Movies.COLUMN_NAME_BACKDROP_URL)
    val backdropUrl: String,

    @ColumnInfo(name = Movies.COLUMN_NAME_RATINGS)
    val ratings: Int,

    @ColumnInfo(name = Movies.COLUMN_NAME_REVIEWS)
    val reviews: Int,

    @ColumnInfo(name = Movies.COLUMN_NAME_MIN_AGE)
    val minAge: Int,

    @ColumnInfo(name = Movies.COLUMN_NAME_RUNTIME)
    val runtime: Int?,

    @ColumnInfo(name = Movies.COLUMN_NAME_GENRES)
    val genres: String
)