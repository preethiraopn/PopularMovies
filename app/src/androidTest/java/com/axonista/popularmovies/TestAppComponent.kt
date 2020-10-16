package com.axonista.popularmovies

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.axonista.popularmovies.di.AppModule
import com.axonista.popularmovies.di.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class]
)
interface TestAppComponent : AndroidInjector<TestApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TestAppComponent
    }

    override fun inject(myApp: TestApp?)
    fun vmFactory(): ViewModelProvider.Factory?
}