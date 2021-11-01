package com.mortex.launchertest

import android.app.Application
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LauncherApp : Application() {

    override fun onCreate(){
        super.onCreate()
    }
}