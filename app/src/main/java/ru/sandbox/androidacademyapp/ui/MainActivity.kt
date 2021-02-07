package ru.sandbox.androidacademyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment.*
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment.*
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment
import ru.sandbox.androidacademyapp.ui.movies.MoviesViewPagerFragment

class MainActivity : AppCompatActivity(),
                     MovieListItemClickListener,
                     BackToMovieListClickListener {

    private var moviesViewPagerFragment: MoviesViewPagerFragment? = null
    private var movieDetailsFragment: MovieDetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            moviesViewPagerFragment = MoviesViewPagerFragment()
            moviesViewPagerFragment?.apply {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragments_container, this)
                    .commit()
            }
        }
    }

    override fun moveToMovieDetailsFragment(movieId: Int) {
        movieDetailsFragment = MovieDetailsFragment.newInstance(movieId)
        movieDetailsFragment?.apply {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .addToBackStack(null)
                .add(R.id.fragments_container, this)
                .commit()
        }
    }

    override fun backToMoviesListFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }
}