package ru.sandbox.androidacademyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment.*
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment.*
import ru.sandbox.androidacademyapp.ui.moviedetails.MovieDetailsFragment
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity(), MovieItemClickListener, BackButtonClickListener {

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

    override fun moveToMovieDetails(movieId: Int, sharedView: View) {
        supportFragmentManager.popBackStack(
            MovieDetailsFragment.TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        supportFragmentManager.commit {
            addSharedElement(sharedView, sharedView.transitionName)
            addToBackStack(MovieDetailsFragment.TAG)
            add(R.id.fragments_container, MovieDetailsFragment.newInstance(movieId, sharedView.transitionName))
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
//                if (id != null) moveToMovieDetails(id)
            }
        }
    }
}