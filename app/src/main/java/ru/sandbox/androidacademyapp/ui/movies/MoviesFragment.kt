package ru.sandbox.androidacademyapp.ui.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.ui.movies.adapters.MoviesRecyclerAdapter.OnRecyclerItemClicked
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MoviesViewModelFactory
import ru.sandbox.androidacademyapp.ui.movies.adapters.MoviesRecyclerAdapter

class MoviesFragment : Fragment(R.layout.fragment_movies_list) {

    interface MovieListItemClickListener {
        fun moveToMovieDetailsFragment(movieId: Int)
    }

    private var listener: MovieListItemClickListener? = null
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var type: String

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(PARAM_TYPE, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orientation: Int = requireActivity().resources.configuration.orientation

        initViews(view)
        initLayoutManager(orientation)
        initItemDecoration(orientation)

        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateMoviesAdapter)
        viewModel.isLoading.observe(this.viewLifecycleOwner, this::showProgressBar)
        viewModel.errorMessage.observe(this.viewLifecycleOwner, this::showToast)

        if (savedInstanceState == null){
            viewModel.loadMovies(type)
        }
    }

    private fun initViews(view: View) {
        progressBar = view.findViewById(R.id.movies_progress_bar)
        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler.adapter = MoviesRecyclerAdapter(clickListener)
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
        (recycler.adapter as? MoviesRecyclerAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private fun showProgressBar(isVisible: Boolean){
        progressBar.isVisible = isVisible
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieListItemClickListener) listener = context
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private val clickListener = object : OnRecyclerItemClicked {
        override fun onClick(movieId: Int) {
            listener?.moveToMovieDetailsFragment(movieId)
        }
    }

    companion object {
        private const val PARAM_TYPE = "type"

        fun newInstance(type: String) =
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_TYPE, type)
                }
            }
    }
}