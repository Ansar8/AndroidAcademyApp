package ru.sandbox.androidacademyapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.MoviesAdapter.OnRecyclerItemClicked

class FragmentMoviesList : Fragment() {

    private var listener: MoviesListFragmentClickListener? = null
    private lateinit var recycler: RecyclerView
    private lateinit var moviesLoadingIssueTextView: TextView

    private val viewModel: MoviesViewModel by viewModels { MoviesViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orientation: Int = requireActivity().resources.configuration.orientation

        val adapter = MoviesAdapter(clickListener)
        recycler = view.findViewById(R.id.recycler_view_movies)
        recycler.adapter = adapter

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.layoutManager = GridLayoutManager(requireContext(), 2)
            recycler.addItemDecoration(MoviesItemDecoration(30, 2))
        }
        else {
            recycler.layoutManager = GridLayoutManager(requireContext(), 4)
            recycler.addItemDecoration(MoviesItemDecoration(30, 4))
        }

        moviesLoadingIssueTextView = view.findViewById(R.id.movies_loading_issue_tv)

        viewModel.movies.observe(this.viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MoviesListFragmentClickListener) listener = context
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

    interface MoviesListFragmentClickListener {
        fun moveToMovieDetailsFragment(movieId: Int)
    }
}