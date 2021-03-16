package ru.sandbox.androidacademyapp.ui.moviesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.rx2.rxObservable
import ru.sandbox.androidacademyapp.data.db.entities.Movie
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.Response
import java.util.concurrent.TimeUnit

class MovieSearchViewModel(private val repository: IMovieRepository): ViewModel() {

    private val _isLoading = MutableLiveData<SearchState>()
    val isLoading: LiveData<SearchState> = _isLoading

    private val _searchResult = MutableLiveData<NetworkState>()
    val searchResult: LiveData<NetworkState> = _searchResult

    val queryInput = PublishSubject.create<String>()

    private val subscriptions = CompositeDisposable()

    init {
        queryInput
            .distinctUntilChanged()
            .doOnNext { item -> Log.d("Next item:", "Next movie's name: $item") }
            .debounce(DEBOUNCE_DELAY_TIME_MS, TimeUnit.MILLISECONDS)
            .doOnEach { _isLoading.postValue(SearchState.Loading) }
            .switchMap(::searchMoviesByQuery)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach { _isLoading.value = SearchState.Ready }
            .subscribeBy(onNext = _searchResult::setValue)
            .addTo(subscriptions)
    }

    private fun searchMoviesByQuery(query: String, page: Int = 1): Observable<NetworkState> {
        return if (query.isEmpty()) {
            Observable.just(NetworkState.EmptyQuery)
        } else {
            rxObservable { send(repository.searchMovies(query, page)) }
                .map { response ->
                    if (response.isEmpty()) {
                        NetworkState.EmptyContent
                    }
                    else{
                        NetworkState.Success(response)
                    }
                }
                .onErrorReturn{ error -> NetworkState.Error(error)}
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    companion object {
        const val DEBOUNCE_DELAY_TIME_MS = 500L
    }
}