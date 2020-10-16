package com.axonista.popularmovies

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton


/**
 * common part of the app which provides CoroutineDispatcher this will help in unit testing
 */
@Singleton
open class AppExecutors(
    private val diskIO:CoroutineDispatcher,
    private val networkIO: CoroutineDispatcher,
    private val mainThread: CoroutineDispatcher
) {

    @Inject
    constructor() : this(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )

    fun diskIO(): CoroutineDispatcher {
        return diskIO
    }

    fun networkIO(): CoroutineDispatcher {
        return networkIO
    }

    fun mainThread(): CoroutineDispatcher {
        return mainThread
    }
}
