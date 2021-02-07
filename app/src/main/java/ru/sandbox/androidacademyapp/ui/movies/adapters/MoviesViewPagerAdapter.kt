package ru.sandbox.androidacademyapp.ui.movies.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment

class MoviesViewPagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm,lc) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MoviesFragment.newInstance("popular")
            1 -> MoviesFragment.newInstance("latest")
            2 -> MoviesFragment.newInstance("top_rated")
            else -> MoviesFragment.newInstance("popular")
        }
    }

    override fun getItemCount(): Int = 3
}