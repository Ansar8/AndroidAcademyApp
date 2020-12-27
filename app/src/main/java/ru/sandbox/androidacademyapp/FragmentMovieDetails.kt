package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sandbox.androidacademyapp.data.Movie
import kotlin.math.roundToInt

class FragmentMovieDetails : Fragment() {

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }

    private var listener: MovieDetailsFragmentClickListener? = null
    private var recycler: RecyclerView? = null

    private lateinit var ratingStars: List<ImageView>
    private var movie: Movie? = null
    private var movieId: Int? = null

    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewModelFactory: MoviesViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt(PARAM_MOVIE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        viewModelFactory = MoviesViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(MoviesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie = viewModel.getMovieById(movieId)

        view.findViewById<ImageView>(R.id.movie_backdrop)
            .apply { Glide.with(context).load(movie?.backdrop).into(this)  }
        view.findViewById<TextView>(R.id.movie_name)
            .apply { text = movie?.title }
        view.findViewById<TextView>(R.id.movie_genre)
            .apply { text = movie?.genres?.joinToString { it.name } }
        view.findViewById<TextView>(R.id.movie_age_limits)
            .apply { text = context.getString(R.string.movie_age_limits_text, movie?.minimumAge.toString()) }
        view.findViewById<TextView>(R.id.movie_reviews)
            .apply { text = context.getString(R.string.movie_reviews_text, movie?.numberOfRatings.toString())}
        view.findViewById<TextView>(R.id.back_text)
            .setOnClickListener { listener?.backToMoviesListFragment() }

        ratingStars = listOf(
            view.findViewById(R.id.movie_star_1),
            view.findViewById(R.id.movie_star_2),
            view.findViewById(R.id.movie_star_3),
            view.findViewById(R.id.movie_star_4),
            view.findViewById(R.id.movie_star_5)
        )
        val ratingOutOfFive = movie?.ratings?.div(10)?.times(5)
        if (ratingOutOfFive != null) {
            showStarRating(ratingOutOfFive.roundToInt())
        }
        else{
            showStarRating(0)
        }

        recycler = view.findViewById(R.id.movie_recycler_view_actors)
        recycler?.adapter = ActorsAdapter()
        recycler?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recycler?.addItemDecoration(ActorsItemDecoration(15))
    }

    private fun showStarRating(rating: Int) {
        for (i in ratingStars.indices){
            if (i < rating)
                ratingStars[i].setImageResource(R.drawable.red_star)
            else
                ratingStars[i].setImageResource(R.drawable.grey_star)
        }
    }

    private fun updateActorsAdapter() {
        (recycler?.adapter as? ActorsAdapter)?.apply {
            movie?.actors?.let { bindMovies(it) }
        }
    }

    //communication with activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClickListener) listener = context
    }

    override fun onStart() {
        super.onStart()
        updateActorsAdapter()
    }

    override fun onDetach() {
        listener = null
        recycler = null
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