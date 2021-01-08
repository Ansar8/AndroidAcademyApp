package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.MoviesAdapter.*
import ru.sandbox.androidacademyapp.data.Movie

class FragmentMoviesList : Fragment() {

    private var listener: MoviesListFragmentClickListener? = null
    private lateinit var recycler: RecyclerView

    private val viewModel: MoviesViewModel by activityViewModels { MoviesViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler.adapter = MoviesAdapter(clickListener)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.addItemDecoration(MoviesItemDecoration(30, 2))

        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateMoviesAdapter)
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MoviesListFragmentClickListener) listener = context
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun updateData() {
        viewModel.loadMovies()
    }

    private fun updateMoviesAdapter(movies: List<Movie>){
        (recycler.adapter as? MoviesAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private val clickListener = object : OnRecyclerItemClicked {
        override fun onClick(movieId: Int) {
            listener?.moveToMovieDetailsFragment(movieId)
        }
    }

    interface MoviesListFragmentClickListener {
        fun moveToMovieDetailsFragment(movieId: Int)
    }
}