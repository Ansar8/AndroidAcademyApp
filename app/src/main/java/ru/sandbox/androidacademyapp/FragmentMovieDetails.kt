package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sandbox.androidacademyapp.data.Actor
import ru.sandbox.androidacademyapp.data.Movie

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }

    private var listener: MovieDetailsFragmentClickListener? = null

    private lateinit var actorsLoadingIssueTextView: TextView
    private lateinit var backdrop: ImageView
    private lateinit var name: TextView
    private lateinit var genre: TextView
    private lateinit var ageLimits: TextView
    private lateinit var reviews: TextView
    private lateinit var storyLine: TextView
    private lateinit var backText: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var ratingStars: List<ImageView>

    private var movieId: Int = -1

    private val viewModel: MoviesViewModel by activityViewModels { MoviesViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt(PARAM_MOVIE_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        viewModel.movieDetails.observe(this.viewLifecycleOwner, this::updateMovieDetails)
        viewModel.actorList.observe(this.viewLifecycleOwner, this::updateActorsAdapter)
        viewModel.isActorsLoadingError.observe(this.viewLifecycleOwner, this::showActorsNotLoadedMessage)

        if (savedInstanceState == null) {
            viewModel.getMovieDetails(movieId)
            viewModel.loadActors(movieId)
        }
    }

    private fun initViews(view: View){
        backdrop = view.findViewById(R.id.movie_backdrop)
        name = view.findViewById(R.id.movie_name)
        genre = view.findViewById(R.id.movie_genre)
        ageLimits = view.findViewById(R.id.movie_age_limits)
        reviews = view.findViewById(R.id.movie_reviews)
        storyLine = view.findViewById(R.id.movie_story_line_text)
        backText = view.findViewById(R.id.back_text)
        backText.setOnClickListener { listener?.backToMoviesListFragment() }

        ratingStars = listOf(
            view.findViewById(R.id.movie_star_1),
            view.findViewById(R.id.movie_star_2),
            view.findViewById(R.id.movie_star_3),
            view.findViewById(R.id.movie_star_4),
            view.findViewById(R.id.movie_star_5)
        )

        recycler = view.findViewById(R.id.movie_recycler_view_actors)
        recycler.adapter = ActorsAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recycler.addItemDecoration(ActorsItemDecoration(15))

        actorsLoadingIssueTextView = view.findViewById(R.id.actors_loading_issue_tv)
    }

    private fun updateMovieDetails(movie: Movie) {
        val context = requireContext()
        Glide.with(context).load(movie.backdropUrl).into(backdrop)// TODO: add placeholder
        name.text = movie.title
        genre.text = movie.genres.joinToString { it.name }
        ageLimits.text = context.getString(R.string.movie_age_limits_text, movie.minimumAge.toString())
        reviews.text = context.getString(R.string.movie_reviews_text, movie.numberOfRatings.toString())
        storyLine.text = movie.overview ?: context.getString(R.string.no_overview_text)
        showStarRating(movie.ratingOutOfFive)
    }

    private fun showStarRating(rating: Int) {
        for (i in ratingStars.indices){
            if (i < rating)
                ratingStars[i].setImageResource(R.drawable.red_star)
            else
                ratingStars[i].setImageResource(R.drawable.grey_star)
        }
    }

    private fun updateActorsAdapter(actors: List<Actor>) {
        (recycler.adapter as? ActorsAdapter)?.apply {
            bindMovies(actors)
        }
    }

    private fun showActorsNotLoadedMessage(isVisible: Boolean){
        actorsLoadingIssueTextView.isVisible = isVisible
        recycler.isVisible = !isVisible
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClickListener) listener = context
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    companion object{
        private const val PARAM_MOVIE_ID = "movieId"

        fun newInstance(movieId: Int): FragmentMovieDetails {
            val fragment = FragmentMovieDetails()
            val args = Bundle()
            args.putInt(PARAM_MOVIE_ID, movieId)
            fragment.arguments = args
            return fragment
        }
    }
}