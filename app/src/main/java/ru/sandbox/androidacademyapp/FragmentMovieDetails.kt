package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentMovieDetails : Fragment() {

    private var listener: MovieDetailsFragmentClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.back_text)
            .setOnClickListener { listener?.backToMoviesListFragment() }
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClickListener) listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }
}