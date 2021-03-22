package ru.sandbox.androidacademyapp.ui.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MoviesViewModelFactory
import ru.sandbox.androidacademyapp.ui.Navigator
import ru.sandbox.androidacademyapp.util.LoadState

class MoviesFragment : Fragment(R.layout.fragment_movies_list) {

    private var listener: Navigator? = null
    private lateinit var movieList: RecyclerView
    private lateinit var moviesPlaceholder: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel.moviesResult.observe(this.viewLifecycleOwner, this::handleMoviesResult)
        viewModel.isLoading.observe(this.viewLifecycleOwner, this::handleLoadingState)
        viewModel.errorMessage.observe(this.viewLifecycleOwner, this::showToast)
    }

    private fun initViews(view: View) {
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }

        movieList = view.findViewById(R.id.recycler_view_movies)
        movieList.layoutManager = GridLayoutManager(requireContext(), spanCount)
        movieList.addItemDecoration(MoviesItemDecoration(30, spanCount))
        movieList.adapter = MoviesAdapter(::moveToMovieDetails)

        moviesPlaceholder = view.findViewById(R.id.movies_placeholder)

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer)
        swipeRefreshLayout.setColorSchemeResources(R.color.pink)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMovies()
        }

        view.findViewById<ImageView>(R.id.movie_list_search_icon).setOnClickListener {
            listener?.moveToMovieSearchFragment()
        }
    }

    private fun updateMoviesAdapter(movies: List<Movie>){
        (movieList.adapter as? MoviesAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private fun handleLoadingState(state: LoadState){
        when(state){
            LoadState.Loading -> swipeRefreshLayout.isRefreshing = true
            LoadState.Ready -> swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun handleMoviesResult(result: MoviesResult){
        when(result){
            is MoviesResult.Success -> {
                updateMoviesAdapter(result.movieList)
                movieList.isVisible = true
                moviesPlaceholder.isVisible = false
            }
            is MoviesResult.EmptyContent -> {
                movieList.isVisible = false
                moviesPlaceholder.isVisible = true
                moviesPlaceholder.setText(R.string.movies_empty_content)
            }
            is MoviesResult.ErrorWithCache -> {
                updateMoviesAdapter(result.cachedMovieList)
                movieList.isVisible = true
                moviesPlaceholder.isVisible = false

                Log.e(MoviesFragment::class.java.name, "Something went wrong.", result.error)
            }
            is MoviesResult.Error -> {
                movieList.isVisible = false
                moviesPlaceholder.isVisible = true
                moviesPlaceholder.setText(R.string.movies_load_error)

                Log.e(MoviesFragment::class.java.name, "Something went wrong.", result.error)
            }
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