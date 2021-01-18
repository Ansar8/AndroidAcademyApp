package ru.sandbox.androidacademyapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.MoviesAdapter.OnRecyclerItemClicked

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var listener: MoviesListFragmentClickListener? = null
    private lateinit var adapter: MoviesAdapter

    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button

    private val viewModel: MoviesViewModel by viewModels { MoviesViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orientation: Int = requireActivity().resources.configuration.orientation

        initViews(view)
        initAdapter()
        initLayoutManager(orientation)
        initItemDecoration(orientation)

        viewModel.movies.observe(this.viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun initViews(view: View) {
        recycler = view.findViewById(R.id.recycler_view_movies)
        progressBar = view.findViewById(R.id.movies_progress_bar)
        retryButton = view.findViewById(R.id.movies_retry_button)
    }

    private fun initAdapter() {
        adapter = MoviesAdapter(clickListener)

        retryButton.setOnClickListener { adapter.retry() }
        recycler.adapter = adapter.withLoadStateFooter(
            footer = MoviesLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(
                        requireActivity(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun initLayoutManager(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == MoviesAdapter.MOVIE_ITEM) 1 else 2
                }
            }
            recycler.layoutManager = gridLayoutManager
        }
        else {
            val gridLayoutManager = GridLayoutManager(requireContext(), 4)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == MoviesAdapter.MOVIE_ITEM) 1 else 4
                }
            }
            recycler.layoutManager = gridLayoutManager
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