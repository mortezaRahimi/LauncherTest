package com.mortex.launchertest.ui.app_list

interface AppListener {
    fun appCheckedForBlockList(app:AppInfoToShow)
    fun removeFromBlockList(app: AppInfoToShow)
}