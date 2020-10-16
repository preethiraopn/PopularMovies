package com.axonista.popularmovies.ui.detail



import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.intercepting.SingleActivityFactory
import com.axonista.popularmovies.R
import com.axonista.popularmovies.TestApp
import com.axonista.popularmovies.ui.moviedetail.MovieDetailScrollingActivity
import com.axonista.popularmovies.util.State
import com.axonista.popularmovies.util.TaskExecutorWithIdlingResourceRule
import com.axonista.popularmovies.util.TestUtil
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


/**
 * testing the UI functionality
 */
@RunWith(AndroidJUnit4::class)
class MovieDetailsActivityTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    lateinit var  testApp:TestApp
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //this method is to be callled before launching the activity also mocking the viewmodelfactory classes
    private val injectedFactory: SingleActivityFactory<MovieDetailScrollingActivity> =
        object:SingleActivityFactory<MovieDetailScrollingActivity>(MovieDetailScrollingActivity::class.java){
            override fun create(intent: Intent?): MovieDetailScrollingActivity {
                viewModelFactory = mock(ViewModelProvider.Factory::class.java)
                testApp = InstrumentationRegistry.getTargetContext().getApplicationContext() as TestApp
                viewModelFactory = testApp.daggerTestAppComponent?.vmFactory()!!
                val movieDetailScrollingActivity = MovieDetailScrollingActivity()
                movieDetailScrollingActivity.viewModelFactory =  viewModelFactory
                return movieDetailScrollingActivity
            }
        }

    @get:Rule
    var mActivityTestRule =
        ActivityTestRule(injectedFactory, false, false)

    @Before
    fun init() {
        val intent = Intent(InstrumentationRegistry.getTargetContext(), MovieDetailScrollingActivity::class.java)
        mActivityTestRule.launchActivity(intent)
    }


    /**
     * checking for the loading status
     */
    @Test
    fun testLoading() {
        Thread.sleep(3000)
        mActivityTestRule.activity.viewModel._movieDetailLiveData.postValue(State.loading())
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }


    /**
     * checking screen view with the value
     */

    @Test
    fun testWithValue() {
        Thread.sleep(3000)
        mActivityTestRule.activity.viewModel._movieDetailLiveData.postValue(State.success(TestUtil.getMovieDetail()))
        onView(withId(R.id.release_year)).check(matches(withText("Release Date:2019-09-09")))
    }

}