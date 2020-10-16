package com.axonista.popularmovies.di


import com.axonista.popularmovies.ui.home.MovieListActivity
import com.axonista.popularmovies.ui.moviedetail.MovieDetailScrollingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): MovieListActivity


    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): MovieDetailScrollingActivity
}

