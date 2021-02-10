package ru.sandbox.androidacademyapp.ui.movies

enum class MoviesPageType(val tabName: String, urlPath: String) {
    POPULAR("Popular","popular"),
    UPCOMING("Upcoming","upcoming"),
    TOPRATED("Top rated","top_rated")
}