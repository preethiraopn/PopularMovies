package com.axonista.popularmovies

import com.axonista.popularmovies.util.CoroutineTestRule

class InstantAppExecutors : AppExecutors(io, main, default) {
    companion object {
        private val io = CoroutineTestRule().testDispatcherProvider.io()
        private val main = CoroutineTestRule().testDispatcherProvider.mainThread()
        private val default = CoroutineTestRule().testDispatcherProvider.default()
    }
}