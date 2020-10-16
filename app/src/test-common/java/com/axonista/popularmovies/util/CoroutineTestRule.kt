package com.axonista.popularmovies.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


/**
 * this is used to mock the coroutine dispatchers
 */
interface DispatcherProvider {

    fun mainThread(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined

}

class DefaultDispatcherProvider : DispatcherProvider


@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun io(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun mainThread(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun unconfined(): CoroutineDispatcher = TestCoroutineDispatcher()
    }


    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}