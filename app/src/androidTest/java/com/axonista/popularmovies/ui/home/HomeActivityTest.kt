package com.axonista.popularmovies.ui.home



import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.intercepting.SingleActivityFactory
import com.axonista.popularmovies.R
import com.axonista.popularmovies.TestApp
import com.axonista.popularmovies.util.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    lateinit var  testApp:TestApp
    lateinit var viewModel:MovieViewModel
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val injectedFactory: SingleActivityFactory<MovieListActivity> =
        object:SingleActivityFactory<MovieListActivity>(MovieListActivity::class.java){
            override fun create(intent: Intent?): MovieListActivity {
                viewModel = mock(MovieViewModel::class.java)
                viewModelFactory = mock(ViewModelProvider.Factory::class.java)
                testApp = InstrumentationRegistry.getTargetContext().getApplicationContext() as TestApp
                viewModelFactory = testApp.daggerTestAppComponent?.vmFactory()!!
                val homeActivity = MovieListActivity()
                homeActivity.viewModelFactory =  viewModelFactory
                return homeActivity
            }
        }

    @get:Rule
    var mActivityTestRule =
        ActivityTestRule(injectedFactory, false, false)

    @Before
    fun init() {
        val intent = Intent(InstrumentationRegistry.getTargetContext(), MovieListActivity::class.java)
        mActivityTestRule.launchActivity(intent)
    }

    @Test
    fun testLoading() {
        Thread.sleep(3000)
        mActivityTestRule.activity.viewModel._moviesLiveData.postValue(State.loading())
        Thread.sleep(1000);
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }



    @Test
    fun testWithValue() {
        Thread.sleep(3000)
        mActivityTestRule.activity.viewModel._moviesLiveData.postValue(State.success(TestUtil.getNewMovieList()))
        Thread.sleep(2000)
        onView(listMatcher().atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(
                ViewMatchers.withText(
                    "factory"
                )
            )
        ))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycler_view_movies)
    }

}