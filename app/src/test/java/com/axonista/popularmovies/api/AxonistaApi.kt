package com.axonista.popularmovies.api


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.axonista.popularmovies.data.network.ApiInterface
import com.axonista.popularmovies.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * this method is to test the api calls
 */

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AxonistaApi {
    private lateinit var service: ApiInterface
    private lateinit var mockWebServer: MockWebServer
    @Before
    fun createService() {
        MockitoAnnotations.initMocks(this);
        mockWebServer = MockWebServer()
        service =  Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    /**
     * testing with different types of api and checking out whether the output from the api is giving required output
     */
    @Test
    fun getMovieList() {
        enqueueResponse("movie_list.json")
        runBlocking {
          val data = service.getMovieList( "test", "en-US",1)
          assertNotNull(data)
          assertEquals(data.body()!!.results.size, 20)
          assertEquals(data.body()!!.results[0]!!.title, "Welcome to Sudden Death")
        }
    }

    /**
     * testing with different types of api and checking out whether the output from the api is giving required output
     */
    @Test
    fun getMovieDetails() {
        enqueueResponse("movie_details.json")
        runBlocking {
            val data = service.getMovieDetailData( 123455,"test", "en-US")
            assertNotNull(data)
            assertEquals(data.body()!!.genres!!.size, 4)
            assertEquals(data.body()!!.title, "Mulan")
        }
    }

    /**
     * we are reading dummy data the from the resource files
     */
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
    val body = source.readString(Charsets.UTF_8)
        mockWebServer.enqueue(
            mockResponse
                .setBody(body)
        )
    }
}
