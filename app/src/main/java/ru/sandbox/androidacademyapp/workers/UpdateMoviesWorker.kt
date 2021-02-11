package ru.sandbox.androidacademyapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.Response

class UpdateMoviesWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: IMovieRepository)
        : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
//            // refresh movies cache
//            Log.d(TAG, "doWork: Started to update movies cache.")
//            //TODO: update all specified types of movie lists
//            when (val result = repository.getMoviesByType("popular")) {
//                is Response.Success -> {
//                    Log.d(TAG, "doWork: Loaded movies from network.")
//                    result.data?.let { remoteMovies ->
//                        repository.saveMovies(remoteMovies)
//                        Log.d(TAG, "doWork: Saved movies to cache.")
//                    }
//                }
//                is Response.Error -> throw Exception("Failed to load movies from network.")
//            }
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "doWork: " + e.message)
            Result.failure()
        }
    }

    companion object {
        val TAG = "UpdateMoviesWorker"
    }
}