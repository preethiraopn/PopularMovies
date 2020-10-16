package com.axonista.popularmovies.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axonista.popularmovies.AppExecutors
import com.axonista.popularmovies.data.model.MovieList
import com.axonista.popularmovies.data.repositories.MovieListRepository
import com.axonista.popularmovies.testing.OpenForTesting


import com.axonista.popularmovies.util.ApiException
import com.axonista.popularmovies.util.AppConstant
import com.axonista.popularmovies.util.NoInternetException
import com.axonista.popularmovies.util.State
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OpenForTesting
class MovieViewModel
@Inject constructor(
    private  val appExecutors: AppExecutors,private val repository: MovieListRepository
) : ViewModel() {
    private var pageIndex = 1
    private var totalMovies = 0
    private var movieList = ArrayList<MovieList.Movie?>()
    val _moviesLiveData = MutableLiveData<State<ArrayList<MovieList.Movie?>>>()
    val moviesLiveData: LiveData<State<ArrayList<MovieList.Movie?>>>
        get() = _moviesLiveData
    private val _loadMoreListLiveData = MutableLiveData<Boolean>()
    val loadMoreListLiveData: LiveData<Boolean>
        get() = _loadMoreListLiveData
    private  var movieResponse: MovieList? = null

    init {
        _loadMoreListLiveData.value = false
    }

    private  val TAG = "MovieViewModel"

    fun getMovies() {
        if (pageIndex == 1) {
            movieList.clear()
            _moviesLiveData.postValue(State.loading())
        } else {
            if (movieList.isNotEmpty() && movieList.last() == null)
                movieList.removeAt(movieList.size - 1)
        }

        viewModelScope.launch(appExecutors.diskIO()) {
             try {
                 movieResponse = repository.getMovies(
                        AppConstant.API_KEY,
                        "en-US",
                        pageIndex)
                 withContext(appExecutors.mainThread()) {
                            movieResponse?.let {
                                movieList.addAll(it.results)
                                totalMovies = it.totalResults
                                _moviesLiveData.postValue(State.success(movieList))
                                _loadMoreListLiveData.value = false
                            }
                    }
                } catch (e: ApiException) {
                    withContext(appExecutors.mainThread()) {
                        _moviesLiveData.postValue(State.error(e.message!!))
                        _loadMoreListLiveData.value = false
                    }
                } catch (e: NoInternetException) {
                    withContext(appExecutors.mainThread()) {
                        _moviesLiveData.postValue(State.error(e.message!!))
                        _loadMoreListLiveData.value = false
                    }
                }
        }
    }

    fun loadMore() {
        pageIndex++
        getMovies()
    }

    fun checkForLoadMoreItems(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (!_loadMoreListLiveData.value!! && (totalItemCount < totalMovies)) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                _loadMoreListLiveData.value = true
            }
        }


    }
}