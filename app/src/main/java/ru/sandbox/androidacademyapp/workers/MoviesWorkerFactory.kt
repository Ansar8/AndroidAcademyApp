package ru.sandbox.androidacademyapp.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.sandbox.androidacademyapp.repository.IMovieRepository

class MoviesWorkerFactory(private val repository: IMovieRepository): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when(workerClassName) {
            UpdateMoviesWorker::class.java.name ->
                UpdateMoviesWorker(appContext, workerParameters, repository)
            else -> null
        }
    }
}