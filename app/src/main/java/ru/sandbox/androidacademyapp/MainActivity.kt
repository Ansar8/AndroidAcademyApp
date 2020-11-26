package ru.sandbox.androidacademyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(),
                     FragmentMoviesList.NavigationFragmentClicks,
                     FragmentMovieDetails.NavigationFragmentClicks {


    private val moviesListFragment =  FragmentMoviesList()
        .apply { setClickListener(this@MainActivity) }

    private val movieDetailsFragment = FragmentMovieDetails()
        .apply { setClickListener(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .apply {
                    add(R.id.fragments_container, moviesListFragment)
                    commit()
                }
        }
    }

    override fun moveToMovieDetailsFragment() {
        supportFragmentManager.beginTransaction()
            .apply {
                addToBackStack(null)
                add(R.id.fragments_container, movieDetailsFragment)
                commit()
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
}