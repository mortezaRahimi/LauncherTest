package com.mortex.launchertest.ui.app_list

import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon

interface AppListenerLinks {
    fun appTappedForLinks(app: AppInfoWithIcon)
}