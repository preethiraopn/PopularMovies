package com.axonista.popularmovies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axonista.popularmovies.AppExecutors
import com.axonista.popularmovies.data.model.MovieDetail
import com.axonista.popularmovies.data.repositories.MovieDetailRepository
import com.axonista.popularmovies.util.ApiException
import com.axonista.popularmovies.util.AppConstant
import com.axonista.popularmovies.util.NoInternetException
import com.axonista.popularmovies.util.State
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailViewModel
@Inject constructor(
    private  val appExecutors: AppExecutors,
    private val repository: MovieDetailRepository
) : ViewModel() {

    val _movieDetailLiveData = MutableLiveData<State<MovieDetail>>()
    val movieDetailLiveData: LiveData<State<MovieDetail>>
        get() = _movieDetailLiveData
    private lateinit var movieDetailResponse: MovieDetail

    fun getMovieDetail(movieId: Int) {
        _movieDetailLiveData.postValue(State.loading())
        viewModelScope.launch(appExecutors.diskIO()) {
            try {
                movieDetailResponse = repository.getMovieDetail(movieId, AppConstant.API_KEY)
                withContext(appExecutors.mainThread()) {
                    _movieDetailLiveData.postValue(State.success(movieDetailResponse))
                }
            } catch (e: ApiException) {
                withContext(appExecutors.mainThread()) {
                    _movieDetailLiveData.postValue(State.error(e.message!!))
                }
            } catch (e: NoInternetException) {
                withContext(appExecutors.mainThread()) {
                    _movieDetailLiveData.postValue(State.error(e.message!!))
                }
            }
        }
    }

    fun getHour(minutes: Int): String{
        val hour = minutes/60
        val minutes = minutes % 60
        return """${hour}h ${minutes}m"""
    }

    fun getGenre(genreList: ArrayList<MovieDetail.Genre>):String{
       var genres = "";
        for(genre in genreList){
            genres += genre.name + " "
        }
        return genres
    }

}