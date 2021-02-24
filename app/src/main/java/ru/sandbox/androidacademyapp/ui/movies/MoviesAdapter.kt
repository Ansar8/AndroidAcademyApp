package ru.sandbox.androidacademyapp.ui.movies

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie

class MoviesAdapter(private val clickListener: OnRecyclerItemClicked): RecyclerView.Adapter<MovieViewHolder>() {

    interface OnRecyclerItemClicked {
        fun onClick(movieId: Int, view: View)
    }

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.view_holder_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.onBind(movie)
        holder.itemView.transitionName = "Transition name: ${movie.title}"
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position].id, it)
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
        showStarRating(movie.ratings)

        Glide.with(context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_movie)
            .error(R.drawable.ic_error)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(poster)

        like.setImageResource(R.drawable.grey_like)
        ageLimits.text = context.getString(R.string.movie_age_limits_text, movie.minAge.toString())
        genre.text = movie.genres
        reviews.text = context.getString(R.string.movie_reviews_text, movie.reviews.toString())
        name.text = movie.title

        val runtime = movie.runtime.toString()
        duration.text = if (runtime == "null") "" else context.getString(R.string.movie_duration_text, runtime)
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