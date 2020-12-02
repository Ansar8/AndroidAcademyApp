package ru.sandbox.androidacademyapp

import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.data.models.Movie

class MoviesAdapter: RecyclerView.Adapter<MovieViewHolder>() {

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.view_holder_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun bindMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ratingStars: List<ImageView?> = listOf(
        itemView.findViewById(R.id.star_1),
        itemView.findViewById(R.id.star_2),
        itemView.findViewById(R.id.star_3),
        itemView.findViewById(R.id.star_4),
        itemView.findViewById(R.id.star_5)
    )

    private val image: ImageView? =
        itemView.findViewById<ImageView?>(R.id.movie_image).apply { setRoundedTopCorners(this) }
    private val like: ImageView? = itemView.findViewById(R.id.movie_like)
    private val ageLimits: TextView? = itemView.findViewById(R.id.movie_age_limits)
    private val genre: TextView? = itemView.findViewById(R.id.movie_genre)
    private val reviews: TextView? = itemView.findViewById(R.id.movie_reviews)
    private val name: TextView? = itemView.findViewById(R.id.movie_name)
    private val duration: TextView? = itemView.findViewById(R.id.movie_duration)

    fun onBind(movie: Movie) {
        val likeImage = if (movie.hasLike) R.drawable.red_like else R.drawable.grey_like
        showStarRating(movie.rating)

        image?.background = ContextCompat.getDrawable(context, movie.image)
        like?.setImageResource(likeImage)
        ageLimits?.text = context.getString(R.string.movie_age_limits_text, movie.ageLimits.toString())
        genre?.text = movie.genre
        reviews?.text = context.getString(R.string.movie_reviews_text, movie.reviews.toString())
        name?.text = movie.name
        duration?.text = context.getString(R.string.movie_duration_text, movie.duration.toString())
    }

    private fun showStarRating(rating: Int) {
        for (i in ratingStars.indices){
            if (i < rating)
                ratingStars[i]?.setImageResource(R.drawable.red_star)
            else
                ratingStars[i]?.setImageResource(R.drawable.grey_star)
        }
    }

    private fun setRoundedTopCorners(image: ImageView?) {
        val curveRadius = 20F

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image?.outlineProvider = object : ViewOutlineProvider() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height+curveRadius).toInt(), curveRadius)
                }
            }
            image?.clipToOutline = true
        }
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context