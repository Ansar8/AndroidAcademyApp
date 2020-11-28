package ru.sandbox.androidacademyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(),
                     FragmentMoviesList.NavigationFragmentClicks,
                     FragmentMovieDetails.NavigationFragmentClicks {


    private var moviesListFragment: FragmentMoviesList? = null
    private var movieDetailsFragment: FragmentMovieDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            moviesListFragment = FragmentMoviesList()
            moviesListFragment?.apply {
                setClickListener(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragments_container, this, MOVIES_LIST_FRAGMENT_FLAG)
                    .commit()
            }
        }
        else {
            moviesListFragment = supportFragmentManager
                .findFragmentByTag(MOVIES_LIST_FRAGMENT_FLAG) as? FragmentMoviesList
            moviesListFragment?.apply { setClickListener(this@MainActivity) }

            movieDetailsFragment = supportFragmentManager
                .findFragmentByTag(MOVIE_DETAILS_FRAGMENT_FLAG) as? FragmentMovieDetails
            movieDetailsFragment?.apply { setClickListener(this@MainActivity) }
        }
    }

    override fun moveToMovieDetailsFragment() {
        movieDetailsFragment = FragmentMovieDetails()
        movieDetailsFragment?.apply {
            setClickListener(this@MainActivity)
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragments_container, this, MOVIE_DETAILS_FRAGMENT_FLAG)
                .commit()
        }
    }

    override fun backToMovieListFragment() {
        if (supportFragmentManager.fragments.size > 1) {
            val lastFragment = supportFragmentManager.fragments.last()
            supportFragmentManager.beginTransaction()
                .apply {
                    remove(lastFragment)
                    commit()
                }
        }
    }

    companion object {
        const val MOVIES_LIST_FRAGMENT_FLAG = "moviesListFragment"
        const val MOVIE_DETAILS_FRAGMENT_FLAG = "movieDetailsFragment"
    }
}