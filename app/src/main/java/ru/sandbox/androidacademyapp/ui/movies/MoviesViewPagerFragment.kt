package ru.sandbox.androidacademyapp.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.movies.adapters.MoviesViewPagerAdapter

class MoviesViewPagerFragment: Fragment(R.layout.fragment_movies_view_pager) {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabLayout)

        setupAdapter()
        setupTabLayout()
    }

    private fun setupAdapter() {
        val pagerAdapter = MoviesViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(MarginPageTransformer(500));
    }

    private fun setupTabLayout(){
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Popular"
                1 -> tab.text = "Upcoming"
                2 -> tab.text = "Top rated"
            }
        }.attach()
    }
}