package ru.sandbox.androidacademyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.moviedetails.FragmentMovieDetails.*
import ru.sandbox.androidacademyapp.ui.movies.FragmentMoviesList.*
import ru.sandbox.androidacademyapp.ui.moviedetails.FragmentMovieDetails
import ru.sandbox.androidacademyapp.ui.movies.FragmentMoviesList

class MainActivity : AppCompatActivity(),
                     MoviesListFragmentClickListener,
                     MovieDetailsFragmentClickListener {

    private var moviesListFragment: FragmentMoviesList? = null
    private var movieDetailsFragment: FragmentMovieDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            moviesListFragment = FragmentMoviesList()
            moviesListFragment?.apply {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragments_container, this, MOVIES_LIST_FRAGMENT_FLAG)
                    .commit()
            }
        }
        else {
            moviesListFragment = supportFragmentManager
                .findFragmentByTag(MOVIES_LIST_FRAGMENT_FLAG) as? FragmentMoviesList

            movieDetailsFragment = supportFragmentManager
                .findFragmentByTag(MOVIE_DETAILS_FRAGMENT_FLAG) as? FragmentMovieDetails
        }
    }

    override fun moveToMovieDetailsFragment(movieId: Int) {
        movieDetailsFragment = FragmentMovieDetails.newInstance(movieId)
        movieDetailsFragment?.apply {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragments_container, this, MOVIE_DETAILS_FRAGMENT_FLAG)
                .commit()
        }
    }

    override fun backToMoviesListFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }

    companion object {
        const val MOVIES_LIST_FRAGMENT_FLAG = "moviesListFragment"
        const val MOVIE_DETAILS_FRAGMENT_FLAG = "movieDetailsFragment"
    }
}