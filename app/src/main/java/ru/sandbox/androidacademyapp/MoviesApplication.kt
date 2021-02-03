package ru.sandbox.androidacademyapp

import android.app.Application
import android.util.Log
import androidx.work.*
import ru.sandbox.androidacademyapp.data.db.MovieDatabase
import ru.sandbox.androidacademyapp.data.network.MoviesApi
import ru.sandbox.androidacademyapp.repository.MovieRepository
import ru.sandbox.androidacademyapp.workers.MoviesWorkerFactory
import ru.sandbox.androidacademyapp.workers.UpdateMoviesWorker
import java.util.concurrent.TimeUnit

class MoviesApplication: Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        val updateMoviesWorkRequest = PeriodicWorkRequest
            .Builder(UpdateMoviesWorker::class.java, 8, TimeUnit.HOURS)
            .setConstraints( Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "updateMovies",
            ExistingPeriodicWorkPolicy.KEEP,
            updateMoviesWorkRequest
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        val repository = MovieRepository(
                MoviesApi.create(),
                MovieDatabase.create(applicationContext).moviesDao
        )
        delegatingWorkerFactory.addFactory(MoviesWorkerFactory(repository))

        return Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .setWorkerFactory(delegatingWorkerFactory)
                .build()
    }
}