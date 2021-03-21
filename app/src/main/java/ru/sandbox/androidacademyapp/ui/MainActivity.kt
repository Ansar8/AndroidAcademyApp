package ru.sandbox.androidacademyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment.*
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment
import ru.sandbox.androidacademyapp.ui.moviesearch.MovieSearchFragment

interface Navigator {
    fun moveToMovieDetailsFragment(movieId: Int)
    fun moveToMovieSearchFragment()
    fun backToMovieList()
}

class MainActivity : AppCompatActivity(), Navigator {

    companion object {
        const val MOVIE_DETAILS_FRAGMENT_FLAG = "movieDetailsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragments_container, MoviesFragment())
            }
            intent?.let(::handleIntent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    override fun moveToMovieDetailsFragment(movieId: Int) {
        supportFragmentManager.popBackStack(
            MOVIE_DETAILS_FRAGMENT_FLAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        supportFragmentManager.commit {
            addToBackStack(MOVIE_DETAILS_FRAGMENT_FLAG)
            add(R.id.fragments_container, MovieDetailsFragment.newInstance(movieId))
        }
    }

    override fun moveToMovieSearchFragment() {
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragments_container, MovieSearchFragment.newInstance())
        }
    }

    override fun backToMovieList() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toIntOrNull()
                if (id != null) moveToMovieDetailsFragment(id)
            }
        }
    }
}