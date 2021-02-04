package ru.sandbox.androidacademyapp.workers

import android.content.Context
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
            // refresh movies cache
            when (val result = repository.getMovies()) {
                is Response.Success -> {
                    result.data?.let { remoteMovies ->
                        repository.saveMovies(remoteMovies)
                    }
                }
                is Response.Error -> throw Exception()
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < MAX_NUMBER_OF_RETRY) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val MAX_NUMBER_OF_RETRY = 3
    }
}