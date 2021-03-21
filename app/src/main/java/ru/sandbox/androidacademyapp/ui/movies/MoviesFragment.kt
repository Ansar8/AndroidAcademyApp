package ru.sandbox.androidacademyapp.ui.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MoviesViewModelFactory
import ru.sandbox.androidacademyapp.ui.Navigator
import ru.sandbox.androidacademyapp.util.LoadState

class MoviesFragment : Fragment(R.layout.fragment_movies_list) {

    private var listener: Navigator? = null
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateMoviesAdapter)
        viewModel.isLoading.observe(this.viewLifecycleOwner, this::handleLoadingState)
        viewModel.errorMessage.observe(this.viewLifecycleOwner, this::showToast)

        if (savedInstanceState == null){
            viewModel.loadMovies()
        }
    }

    private fun initViews(view: View) {
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }

        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler.layoutManager = GridLayoutManager(requireContext(), spanCount)
        recycler.addItemDecoration(MoviesItemDecoration(30, spanCount))
        recycler.adapter = MoviesAdapter(::moveToMovieDetails)

        progressBar = view.findViewById(R.id.movies_progress_bar)

        view.findViewById<ImageView>(R.id.movie_list_search_icon).setOnClickListener {
            listener?.moveToMovieSearchFragment()
        }
    }

    private fun updateMoviesAdapter(movies: List<Movie>){
        (recycler.adapter as? MoviesAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private fun handleLoadingState(state: LoadState){
        when(state){
            LoadState.Loading -> progressBar.isVisible = true
            LoadState.Ready -> progressBar.isVisible = false
        }

    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Navigator) listener = context
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun moveToMovieDetails(movie: Movie) {
        listener?.moveToMovieDetailsFragment(movie.id)
    }
}