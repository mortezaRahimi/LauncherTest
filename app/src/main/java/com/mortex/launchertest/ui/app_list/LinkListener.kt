package com.mortex.launchertest.ui.app_list

import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.local.ULink

interface LinkListener {
    fun linkTapped(link:ULink)
}