package com.axonista.popularmovies.di

import android.app.Application
import com.axonista.popularmovies.data.network.ApiInterface
import com.axonista.popularmovies.data.network.NetworkConnectionInterceptor


import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import javax.inject.Singleton

/**
 * - retrofit client creating
 * - added offline caching as a temparary purpose( we can also replace it with room database but for the current
 * situation retrofit offline options seemed a better approach
 */

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideMindValleyService(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(networkConnectionInterceptor.applicationContext.cacheDir, cacheSize)
        val okkHttpclient = OkHttpClient.Builder().cache(myCache)
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkInterceptors(app: Application): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(app)
    }


}
