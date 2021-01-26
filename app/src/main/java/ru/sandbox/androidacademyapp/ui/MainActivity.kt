package ru.sandbox.androidacademyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment.*
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment.*
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity(),
                     MoviesListFragmentClickListener,
                     MovieDetailsFragmentClickListener {

    private var moviesFragment: MoviesFragment? = null
    private var movieDetailsFragment: MovieDetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            moviesFragment = MoviesFragment()
            moviesFragment?.apply {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragments_container, this, MOVIES_LIST_FRAGMENT_FLAG)
                    .commit()
            }
        }
        else {
            moviesFragment = supportFragmentManager
                .findFragmentByTag(MOVIES_LIST_FRAGMENT_FLAG) as? MoviesFragment

            movieDetailsFragment = supportFragmentManager
                .findFragmentByTag(MOVIE_DETAILS_FRAGMENT_FLAG) as? MovieDetailsFragment
        }
    }

    override fun moveToMovieDetailsFragment(movieId: Int) {
        movieDetailsFragment = MovieDetailsFragment.newInstance(movieId)
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