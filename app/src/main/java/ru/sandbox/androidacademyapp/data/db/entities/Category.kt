package ru.sandbox.androidacademyapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract

@Entity(tableName = MoviesDbContract.Categories.TABLE_NAME)
data class Category(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MoviesDbContract.Categories.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = MoviesDbContract.Categories.COLUMN_NAME_NAME)
    val name: String,
)