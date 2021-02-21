package ru.sandbox.androidacademyapp

import android.app.Application
import android.util.Log
import androidx.work.*
import ru.sandbox.androidacademyapp.data.db.MovieDatabase
import ru.sandbox.androidacademyapp.data.network.MoviesApi
import ru.sandbox.androidacademyapp.notifications.NewMovieNotifications
import ru.sandbox.androidacademyapp.repository.MovieRepository
import ru.sandbox.androidacademyapp.workers.MoviesWorkerFactory
import ru.sandbox.androidacademyapp.workers.UpdateMoviesWorker
import java.util.concurrent.TimeUnit

class MoviesApplication: Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        //setOneTimeCacheUpdate()
    }

    fun setOneTimeCacheUpdate(){
        //Use a one time work request to check that WM works as expected
        //instead of managing cancellation of periodic request after app is tested.

        val simpleRequest = OneTimeWorkRequest.Builder(UpdateMoviesWorker::class.java)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .setConstraints( Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            ).build()

        //Track the live data of a work request state

        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(simpleRequest.id)
            .observeForever { workInfo ->
                Log.d("-> state", workInfo.state.toString())
            }

        //Schedule a one time work request
        WorkManager.getInstance(this).enqueue(simpleRequest)
    }

    fun setPeriodicCacheUpdate(){
        //An example how to enqueue unique periodic work

        val updateMoviesWorkRequest = PeriodicWorkRequest
            .Builder(UpdateMoviesWorker::class.java, 8, TimeUnit.HOURS)
            .setConstraints( Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            )
            .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "updateMoviesWork",
            ExistingPeriodicWorkPolicy.KEEP,
            updateMoviesWorkRequest
        )

        //An example how to cancel a unique periodic work by it's name
        WorkManager.getInstance(this).cancelUniqueWork("updateMoviesWork")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        val repository = MovieRepository(
                MoviesApi.create(),
                MovieDatabase.create(applicationContext).moviesDao,
                NewMovieNotifications(applicationContext)
        )
        delegatingWorkerFactory.addFactory(MoviesWorkerFactory(repository))

        return Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setWorkerFactory(delegatingWorkerFactory)
                .build()
    }
}