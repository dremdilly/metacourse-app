package com.example.metacourse

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        PreferenceManager.initPreferences(applicationContext)

        if(com.example.metacourse.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}