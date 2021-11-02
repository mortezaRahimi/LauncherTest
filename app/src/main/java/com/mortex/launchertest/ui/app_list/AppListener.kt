package com.mortex.launchertest.ui.app_list

import com.mortex.launchertest.local.AppInfo

interface AppListener {
    fun appTapped(app: AppInfo)
}