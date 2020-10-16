package com.axonista.popularmovies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.axonista.popularmovies.ui.home.MovieViewModel
import com.axonista.popularmovies.ui.moviedetail.MovieDetailViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * creates the viewmodel and stores in the factory map
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    abstract fun bindHomeViewModel(movieViewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailModel(movieDetailModel:  MovieDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AxonistaViewModelFactory): ViewModelProvider.Factory
}
