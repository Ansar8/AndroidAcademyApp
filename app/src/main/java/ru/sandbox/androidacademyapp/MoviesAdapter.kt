package ru.sandbox.androidacademyapp

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sandbox.androidacademyapp.data.Movie
import kotlin.math.roundToInt

class MoviesAdapter(private val clickListener: OnRecyclerItemClicked): RecyclerView.Adapter<MovieViewHolder>() {

    interface OnRecyclerItemClicked {
        fun onClick(movieId: Int)
    }

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.view_holder_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position].id)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun bindMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ratingStars: List<ImageView> = listOf(
        itemView.findViewById(R.id.star_1),
        itemView.findViewById(R.id.star_2),
        itemView.findViewById(R.id.star_3),
        itemView.findViewById(R.id.star_4),
        itemView.findViewById(R.id.star_5)
    )

    private val poster: ImageView =
        itemView.findViewById<ImageView>(R.id.poster).apply { setRoundedTopCorners(this) }
    private val like: ImageView = itemView.findViewById(R.id.like)
    private val ageLimits: TextView = itemView.findViewById(R.id.age_limits)
    private val genre: TextView = itemView.findViewById(R.id.genre)
    private val reviews: TextView = itemView.findViewById(R.id.reviews)
    private val name: TextView = itemView.findViewById(R.id.movie_name)
    private val duration: TextView = itemView.findViewById(R.id.duration)

    fun onBind(movie: Movie) {
        val ratingOutOfFive = 5 * movie.ratings / 10
        showStarRating(ratingOutOfFive.roundToInt())

        Glide.with(context).load(movie.poster).into(poster)

        like.setImageResource(R.drawable.grey_like)
        ageLimits.text = context.getString(R.string.movie_age_limits_text, movie.minimumAge.toString())
        genre.text = movie.genres.joinToString { it.name }
        reviews.text = context.getString(R.string.movie_reviews_text, movie.numberOfRatings.toString())
        name.text = movie.title
        duration.text = context.getString(R.string.movie_duration_text, movie.runtime.toString())
    }

    private fun showStarRating(rating: Int) {
        for (i in ratingStars.indices){
            if (i < rating)
                ratingStars[i].setImageResource(R.drawable.red_star)
            else
                ratingStars[i].setImageResource(R.drawable.grey_star)
        }
    }

    private fun setRoundedTopCorners(image: ImageView) {
        val curveRadius = 25F

        image.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline?) {
                outline?.setRoundRect(
                    0,
                    0,
                    view.width,
                    (view.height + curveRadius).toInt(),
                    curveRadius
                )
            }
        }
        image.clipToOutline = true
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context