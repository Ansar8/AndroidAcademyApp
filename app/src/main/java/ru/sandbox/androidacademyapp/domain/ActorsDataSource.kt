package ru.sandbox.androidacademyapp.domain

import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.models.Actor
import ru.sandbox.androidacademyapp.data.models.Movie

class ActorsDataSource {
    fun getActors(): List<Actor>{
        return listOf(
            Actor(R.drawable.robert, "Robert Downey Jr."),
            Actor(R.drawable.mark, "Mark Ruffalo"),
            Actor(R.drawable.chris, "Chris Evans"),
            Actor(R.drawable.chris2, "Chris Hemsworth"),
            Actor(R.drawable.robert, "Robert Downey Jr."),
            Actor(R.drawable.mark, "Mark Ruffalo"),
            Actor(R.drawable.chris, "Chris Evans"),
            Actor(R.drawable.chris2, "Chris Hemsworth")
        )
    }
}