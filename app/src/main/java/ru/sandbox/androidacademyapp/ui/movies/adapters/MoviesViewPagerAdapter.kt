package ru.sandbox.androidacademyapp.ui.movies.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.sandbox.androidacademyapp.ui.movies.MoviesCategory
import ru.sandbox.androidacademyapp.ui.movies.MoviesFragment

class MoviesViewPagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm,lc) {

    override fun createFragment(position: Int): Fragment {
        val category = when (position) {
            0 -> MoviesCategory.POPULAR.name
            1 -> MoviesCategory.UPCOMING.name
            2 -> MoviesCategory.TOPRATED.name
            else -> MoviesCategory.POPULAR.name
        }
        return MoviesFragment.newInstance(category)
    }

    override fun getItemCount(): Int = 3
}