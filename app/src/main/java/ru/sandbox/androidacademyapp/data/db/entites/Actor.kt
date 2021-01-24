package ru.sandbox.androidacademyapp.data.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sandbox.androidacademyapp.data.db.MoviesDbContract.Actors

@Entity(tableName = Actors.TABLE_NAME)
data class Actor(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = Actors.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = Actors.COLUMN_NAME_NAME)
    val name: String,

    @ColumnInfo(name = Actors.COLUMN_NAME_PICTURE)
    val pictureUrl: String?
)