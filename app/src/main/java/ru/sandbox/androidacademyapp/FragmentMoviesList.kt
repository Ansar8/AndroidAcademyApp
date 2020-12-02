package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.data.models.Movie
import ru.sandbox.androidacademyapp.domain.MoviesDataSource

class FragmentMoviesList : Fragment() {

    private var listener: MoviesListFragmentClickListener? = null
    private var recycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler?.adapter = MoviesAdapter(clickListener)
        recycler?.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler?.addItemDecoration(SpacesItemDecoration(30, 2))
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
        recycler = null
        super.onDetach()
    }

    private fun updateData() {
        (recycler?.adapter as? MoviesAdapter)?.apply {
            bindMovies(MoviesDataSource().getMovies())
        }
    }

    private fun doOnClick(movie: Movie) {
        listener?.moveToMovieDetailsFragment()
    }

    private val clickListener = object : OnRecyclerItemClicked {
        override fun onClick(movie: Movie) {
            doOnClick(movie)
        }
    }

    interface MoviesListFragmentClickListener {
        fun moveToMovieDetailsFragment()
    }

}