package ru.sandbox.androidacademyapp.domain

import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.models.Movie

class MoviesDataSource {
    fun getMovies(): List<Movie>{
        return listOf(
            Movie(
                R.drawable.avengers,
                "Avengers: End Game",
                false,
                13,
                "Action, Adventure, Drama",
                4,
                125,
                137
            ),
            Movie(
                R.drawable.tenet,
                "Tenet",
                true,
                16,
                "Action, Sci-Fi, Thriller",
                5,
                98,
                97
            ),
            Movie(
                R.drawable.black_widow,
                "Black Widow",
                false,
                13,
                "Action, Adventure, Sci-Fi",
                4,
                38,
                102
            ),
            Movie(
                R.drawable.wonder_woman,
                "Wonder Woman 1984",
                false,
                13,
                "Action, Adventure, Fantasy",
                5,
                74,
                120
            )
        )
    }
}