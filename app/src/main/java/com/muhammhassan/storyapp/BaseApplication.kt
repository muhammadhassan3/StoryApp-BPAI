package com.muhammhassan.storyapp

import android.app.Application
import com.muhammhassan.storyapp.utils.Module.apiModule
import com.muhammhassan.storyapp.utils.Module.dataSourceModule
import com.muhammhassan.storyapp.utils.Module.repositoryModule
import com.muhammhassan.storyapp.utils.Module.useCaseModule
import com.muhammhassan.storyapp.utils.Module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(
                apiModule,
                viewModelModule,
                useCaseModule,
                dataSourceModule,
                repositoryModule
            ))
        }
    }
}