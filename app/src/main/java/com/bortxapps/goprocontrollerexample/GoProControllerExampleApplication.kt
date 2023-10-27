package com.bortxapps.goprocontrollerexample

import android.app.Application
import com.bortxapps.goprocontrollerexample.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GoProControllerExampleApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@GoProControllerExampleApplication)
            androidLogger()
            modules(appModule)
        }
    }
}