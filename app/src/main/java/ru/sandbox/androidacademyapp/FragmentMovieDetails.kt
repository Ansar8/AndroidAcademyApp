package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.sandbox.androidacademyapp.data.db.entities.relations.MovieWithActors

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }

    private var listener: MovieDetailsFragmentClickListener? = null

    private lateinit var movieFrame: FrameLayout
    private lateinit var backdrop: ImageView
    private lateinit var name: TextView
    private lateinit var genre: TextView
    private lateinit var ageLimits: TextView
    private lateinit var reviews: TextView
    private lateinit var storyLine: TextView
    private lateinit var backText: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var ratingStars: List<ImageView>
    private lateinit var progressBar: ProgressBar
    private lateinit var castTitle: TextView
    private lateinit var storyLineTitle: TextView

    private var movieId: Int = -1

    private val viewModel: MovieDetailsViewModel by viewModels {
        MoviesViewModelFactory(applicationContext = requireContext().applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt(PARAM_MOVIE_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel.isLoading.observe(this.viewLifecycleOwner, this::showProgressBar)
        viewModel.movieDetails.observe(this.viewLifecycleOwner){ movieWithActors ->
            updateMovieDetails(movieWithActors)
            showMovieDetails(true)
        }
        viewModel.errorMessage.observe(this.viewLifecycleOwner, this::showToast)

        if (savedInstanceState == null)
            viewModel.loadMovieDetails(movieId)
    }

    private fun initViews(view: View){
        progressBar = view.findViewById(R.id.movie_details_progress_bar)

        movieFrame = view.findViewById(R.id.movie_frame)
        backdrop = view.findViewById(R.id.movie_backdrop)
        name = view.findViewById(R.id.movie_name)
        genre = view.findViewById(R.id.movie_genre)
        ageLimits = view.findViewById(R.id.movie_age_limits)
        reviews = view.findViewById(R.id.movie_reviews)
        storyLine = view.findViewById(R.id.movie_story_line_text)
        storyLineTitle = view.findViewById(R.id.movie_story_line)
        castTitle = view.findViewById(R.id.movie_cast)

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

        showMovieDetails(false)
    }

    private fun updateMovieDetails(movieWithActors: MovieWithActors) {

        val movie = movieWithActors.movie
        val actors = movieWithActors.actors
        val context = requireContext()

        Glide.with(context)
            .load(movie.backdropUrl)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .error(R.drawable.ic_movie)
            .into(backdrop)

        name.text = movie.title
        genre.text = movie.genres
        ageLimits.text = context.getString(R.string.movie_age_limits_text, movie.minAge.toString())
        reviews.text = context.getString(R.string.movie_reviews_text, movie.reviews.toString())
        storyLine.text = movie.overview ?: context.getString(R.string.no_overview_text)
        setStarRating(movie.ratings)

        (recycler.adapter as? ActorsAdapter)?.apply {
            bindMovies(actors)
        }
    }

    private fun setStarRating(rating: Int) {
        for (i in ratingStars.indices){
            if (i < rating)
                ratingStars[i].setImageResource(R.drawable.red_star)
            else
                ratingStars[i].setImageResource(R.drawable.grey_star)
        }
    }

    private fun showProgressBar(isVisible: Boolean){
        progressBar.isVisible = isVisible
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showMovieDetails(isVisible: Boolean){
        movieFrame.isVisible = isVisible
        ageLimits.isVisible = isVisible
        name.isVisible = isVisible
        genre.isVisible = isVisible
        ratingStars.forEach { it.isVisible = isVisible }
        reviews.isVisible = isVisible
        storyLine.isVisible = isVisible
        storyLineTitle.isVisible = isVisible
        castTitle.isVisible = isVisible
        recycler.isVisible = isVisible
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