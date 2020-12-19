package ru.sandbox.androidacademyapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sandbox.androidacademyapp.data.Movie
import ru.sandbox.androidacademyapp.domain.ActorsDataSource
import kotlin.math.roundToInt

class FragmentMovieDetails : Fragment() {

    interface MovieDetailsFragmentClickListener {
        fun backToMoviesListFragment()
    }

    private var listener: MovieDetailsFragmentClickListener? = null
    private var recycler: RecyclerView? = null

    private lateinit var ratingStars: List<ImageView>
    private var movie: Movie? = null
//    private var movieRating: Int = 0
//    private lateinit var movieName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            movieName = it.getString(PARAM_NAME).toString()
//            movieRating = it.getInt(PARAM_RATING)
            movie = it.getParcelable(PARAM_MOVIE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    companion object{
//        private const val PARAM_NAME = "movie_name"
//        private const val PARAM_RATING = "movie_rating"
        private const val PARAM_MOVIE = "movie_movie"

        fun newInstance(movie: Movie): FragmentMovieDetails {
            val fragment = FragmentMovieDetails()
            val args = Bundle()
            args.putParcelable(PARAM_MOVIE, movie)
//            args.putString(PARAM_NAME, movie.name)
//            args.putInt(PARAM_RATING, movie.rating)
            fragment.arguments = args
            return fragment
        }
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
}