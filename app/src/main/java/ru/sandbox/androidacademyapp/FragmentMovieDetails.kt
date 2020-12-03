package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.domain.ActorsDataSource
import ru.sandbox.androidacademyapp.domain.MoviesDataSource

class FragmentMovieDetails : Fragment() {

    private var listener: MovieDetailsFragmentClickListener? = null
    private var recycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.back_text)
            .setOnClickListener { listener?.backToMoviesListFragment() }
        recycler = view.findViewById(R.id.recycler_view_actors)
        recycler?.adapter = ActorsAdapter()
        recycler?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recycler?.addItemDecoration(ActorsItemDecoration(15))
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClickListener) listener = context
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
        (recycler?.adapter as? ActorsAdapter)?.apply {
            bindMovies(ActorsDataSource().getActors())
        }
    }

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }
}