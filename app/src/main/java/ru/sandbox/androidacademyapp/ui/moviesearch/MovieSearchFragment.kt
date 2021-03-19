package ru.sandbox.androidacademyapp.ui.moviesearch

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MoviesViewModelFactory
import ru.sandbox.androidacademyapp.ui.Navigator
import ru.sandbox.androidacademyapp.ui.movies.MoviesAdapter
import ru.sandbox.androidacademyapp.ui.movies.MoviesItemDecoration
import ru.sandbox.androidacademyapp.util.LoadState


class MovieSearchFragment : Fragment(R.layout.fragment_movie_search) {

    private lateinit var searchInputEditText: EditText
    private lateinit var searchIconImageView: ImageView
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var searchOutputMessage: TextView
    private lateinit var movieListRecyclerView: RecyclerView
    private lateinit var searchBackButton: ImageView

    private var listener: Navigator? = null

    private val viewModel: MovieSearchViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel.searchResult.observe(this.viewLifecycleOwner, this::handleSearchResult)
        viewModel.isLoading.observe(this.viewLifecycleOwner, this::handleLoadingState)
    }

    private fun initViews(view: View) {
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }

        movieListRecyclerView = view.findViewById(R.id.search_movies_rv)
        movieListRecyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        movieListRecyclerView.addItemDecoration(MoviesItemDecoration(30, spanCount))
        movieListRecyclerView.adapter = MoviesAdapter(::moveToMovieDetails)

        searchIconImageView = view.findViewById(R.id.search_icon)
        searchProgressBar = view.findViewById(R.id.movie_search_progress_bar)
        searchOutputMessage = view.findViewById(R.id.search_output_message)
        searchInputEditText = view.findViewById(R.id.search_input)

        searchBackButton = view.findViewById(R.id.back_icon)
        searchBackButton.setOnClickListener { listener?.backToMovieList() }

        searchInputEditText.textChanges()
            .map { text -> text.toString() }
            .subscribe(viewModel.queryInput)
    }

    private fun handleSearchResult(result: SearchResult){
        when(result){
            is SearchResult.Success -> {
                searchOutputMessage.isVisible = false
                movieListRecyclerView.isVisible = true
                updateMovieList(result.movieList)
            }
            is SearchResult.Error -> {
                updateMovieList(emptyList())
                searchOutputMessage.isVisible = true
                movieListRecyclerView.isVisible = false
                searchOutputMessage.setText(R.string.search_error)

                Log.e(MovieSearchFragment::class.java.name, "Something went wrong.", result.error)
            }
            is SearchResult.EmptyContent -> {
                updateMovieList(emptyList())
                searchOutputMessage.isVisible = true
                movieListRecyclerView.isVisible = false
                searchOutputMessage.setText(R.string.empty_content)
            }
            is SearchResult.EmptyQuery -> {
                updateMovieList(emptyList())
                searchOutputMessage.isVisible = true
                movieListRecyclerView.isVisible = false
                searchOutputMessage.setText(R.string.empty_query)
            }
        }
    }

    private fun handleLoadingState(state: LoadState){
        when(state){
            LoadState.Loading -> {
                searchProgressBar.isVisible = true
                searchIconImageView.isVisible = false
            }
            LoadState.Ready -> {
                searchProgressBar.isVisible = false
                searchIconImageView.isVisible = true
            }
        }
    }

    private fun updateMovieList(movies: List<Movie>){
        (movieListRecyclerView.adapter as? MoviesAdapter)?.apply {
            bindMovies(movies)
        }
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

    companion object {
        fun newInstance() = MovieSearchFragment()
    }
}