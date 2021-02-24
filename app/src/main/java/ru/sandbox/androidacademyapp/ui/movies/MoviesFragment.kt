package ru.sandbox.androidacademyapp.ui.movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.core.content.res.use
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.android.synthetic.main.view_holder_movie.*
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MoviesViewModelFactory
import ru.sandbox.androidacademyapp.ui.movies.MoviesAdapter.OnRecyclerItemClicked

class MoviesFragment : Fragment(R.layout.fragment_movies_list) {

    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val orientation: Int = requireActivity().resources.configuration.orientation
        initViews(view)
        initLayoutManager(orientation)
        initItemDecoration(orientation)

        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateMoviesAdapter)
        viewModel.isLoading.observe(this.viewLifecycleOwner, this::showProgressBar)
        viewModel.errorMessage.observe(this.viewLifecycleOwner, this::showToast)

        if (savedInstanceState == null){
            viewModel.loadMovies()
        }
    }

    private fun initViews(view: View) {
        progressBar = view.findViewById(R.id.movies_progress_bar)
        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler.adapter = MoviesAdapter(clickListener)
    }

    private fun initLayoutManager(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        else {
            recycler.layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }

    private fun initItemDecoration(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.addItemDecoration(MoviesItemDecoration(30, 2))
        }
        else {
            recycler.addItemDecoration(MoviesItemDecoration(30, 4))
        }
    }

    private fun updateMoviesAdapter(movies: List<Movie>){
        (recycler.adapter as? MoviesAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private fun showProgressBar(isVisible: Boolean){
        progressBar.isVisible = isVisible
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private val clickListener = object : OnRecyclerItemClicked {
        override fun onClick(movieId: Int, view: View) {

            exitTransition = MaterialElevationScale(false).apply {
                duration = 2000
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 2000
            }

            val bundle = bundleOf("movieId" to movieId)
            val extras = FragmentNavigatorExtras(view to getString(R.string.movie_details_transition_name))
            findNavController().navigate(R.id.navigate_to_movie_details, bundle, null, extras)
        }
    }
}