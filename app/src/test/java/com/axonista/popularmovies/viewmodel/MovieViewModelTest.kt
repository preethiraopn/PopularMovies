package com.axonista.popularmovies.viewmodel




import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.axonista.popularmovies.InstantAppExecutors
import com.axonista.popularmovies.data.model.MovieList
import com.axonista.popularmovies.data.repositories.MovieListRepository
import com.axonista.popularmovies.ui.home.MovieViewModel
import com.axonista.popularmovies.util.*
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.stub
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository:MovieListRepository

    @Mock
    lateinit var observer: Observer<State<ArrayList<MovieList.Movie?>>>

    lateinit var viewModel:MovieViewModel


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        viewModel = MovieViewModel(InstantAppExecutors(),repository)
        viewModel.moviesLiveData.observeForever(observer)
        repository.stub {
            onBlocking {
                repository.getMovies(
                    AppConstant.API_KEY,
                    "en-US",
                    1)
            }.doReturn(TestUtil.getmovies())
        }
    }

    /**
     * this is to test with the loaded data
     */
    @Test
    fun testWithLoadedData() {
        //this method is to mock suspended functions in coroutines
        repository.stub {
            onBlocking {
                repository.getMovies(
                    AppConstant.API_KEY,
                    "en-US",
                    1)
            }.doReturn(TestUtil.getmovies())
        }

            assertNotNull(viewModel.getMovies())
            assertTrue(viewModel.moviesLiveData.value is State.Success)
            assertTrue(viewModel.moviesLiveData.hasObservers())

    }


    /**
     * there is current error with coroutines/mockito which doesn't allow supended function to throw the exception
     */
    @Test
    fun testWithError() {
        assertNotNull(viewModel.getMovies())
        viewModel._moviesLiveData.postValue(State.error("failed"))
        assertTrue(viewModel.moviesLiveData.value is State.Error)
        assertTrue(viewModel.moviesLiveData.hasObservers())
    }




}