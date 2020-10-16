package com.axonista.popularmovies

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.axonista.popularmovies.ui.home.MovieListActivity


class TestApp : Application() {
    var daggerTestAppComponent: TestAppComponent? = null
    var viewModelFactory: ViewModelProvider.Factory? = null
    override fun onCreate() {
        super.onCreate()
        daggerTestAppComponent = DaggerTestAppComponent.builder().application(this).build()
        this.registerActivityLifecycleCallbacks(object:ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is MovieListActivity) {
                    if (viewModelFactory != null) {
                        activity.viewModelFactory = viewModelFactory as ViewModelProvider.Factory
                    }
                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

        })
    }

}
