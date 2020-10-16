package com.axonista.popularmovies

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDex
import com.axonista.popularmovies.di.AppInjector


import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject



class Axonista : Application() , HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    companion object{
        lateinit var instance: Axonista
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppInjector.init(this)
        MultiDex.install(this)
    }


    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector as AndroidInjector<Any>
    }


}
