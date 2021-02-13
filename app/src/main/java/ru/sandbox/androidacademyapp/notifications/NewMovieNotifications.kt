package ru.sandbox.androidacademyapp.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.net.toUri
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.ui.MainActivity

interface Notifications {
    fun showNotification(movie: Movie)
    fun dismissNotification(movieId: Int)
}

class NewMovieNotifications(private val context: Context): Notifications {

    companion object{
        private const val CHANNEL_NEW_MOVIES = "new_movies"
        private const val REQUEST_CODE = 1
        private const val MOVIE_TAG = "movie"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        val channel = NotificationChannelCompat
            .Builder(CHANNEL_NEW_MOVIES, IMPORTANCE_HIGH)
            .setName(context.getString(R.string.channel_new_movies))
            .setDescription(context.getString(R.string.channel_new_movies_description))
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
    }

    override fun showNotification(movie: Movie) {
        val contentUri = "https://android.example.com/movie/${movie.id}".toUri()

        val notification = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle(movie.title)
            .setContentText(context.getString(R.string.channel_new_movies_content_text))
            .setSmallIcon(R.drawable.ic_movie_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CODE,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()

        notificationManagerCompat.notify(MOVIE_TAG, movie.id, notification)
    }

    override fun dismissNotification(movieId: Int) {
        notificationManagerCompat.cancel(MOVIE_TAG, movieId)
    }
}