package ru.sandbox.androidacademyapp.workers

import android.content.Context
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
            when (val result = repository.getMovies()) {
                is ru.sandbox.androidacademyapp.util.Result.Success -> {
                    result.data?.let { remoteMovies ->
                        repository.saveMovies(remoteMovies)
                    }
                }
                is ru.sandbox.androidacademyapp.util.Result.Error -> throw Exception()
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