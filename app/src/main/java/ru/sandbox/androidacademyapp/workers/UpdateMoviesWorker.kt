package ru.sandbox.androidacademyapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class UpdateMoviesWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: IMovieRepository)
        : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // refresh movies cache
            Log.d(TAG, "doWork: Started to update movies cache.")
            val remoteMovies = repository.getMovies()
            if (remoteMovies.isNotEmpty())
                repository.saveMovies(remoteMovies)

            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "doWork: " + e.message)
            Result.failure()
        }
    }

    companion object {
        const val TAG = "UpdateMoviesWorker"
    }
}