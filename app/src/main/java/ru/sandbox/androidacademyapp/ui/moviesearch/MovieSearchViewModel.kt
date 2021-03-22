package ru.sandbox.androidacademyapp.ui.moviesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.rx2.rxObservable
import ru.sandbox.androidacademyapp.repository.IMovieRepository
import ru.sandbox.androidacademyapp.util.LoadState
import java.util.concurrent.TimeUnit

class MovieSearchViewModel(private val repository: IMovieRepository): ViewModel() {

    private val _isLoading = MutableLiveData<LoadState>()
    val isLoading: LiveData<LoadState> = _isLoading

    private val _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult> = _searchResult

    val queryInput = PublishSubject.create<String>()

    private val subscriptions = CompositeDisposable()

    init {
        queryInput
            .debounce(DEBOUNCE_DELAY_TIME_MS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .doOnNext { item -> Log.d("Next item:", "Next movie's name: $item") }
            .doOnEach { _isLoading.postValue(LoadState.Loading) }
            .switchMap(::searchMoviesByQuery)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach { _isLoading.value = LoadState.Ready }
            .subscribeBy(onNext = _searchResult::setValue)
            .addTo(subscriptions)
    }

    private fun searchMoviesByQuery(query: String, page: Int = 1): Observable<SearchResult> {
        return if (query.isEmpty()) {
            Observable.just(SearchResult.EmptyQuery)
        } else {
            rxObservable { send(repository.searchMovies(query, page)) }
                .map { result ->
                    if (result.isEmpty()) {
                        SearchResult.EmptyContent
                    }
                    else{
                        SearchResult.Success(result)
                    }
                }
                .onErrorReturn{ error -> SearchResult.Error(error)}
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