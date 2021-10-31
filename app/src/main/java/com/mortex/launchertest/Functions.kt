package com.mortex.launchertest

import android.content.Intent
import android.content.pm.PackageManager

val appsList: MutableList<AppInfo> = ArrayList()

fun loadApps(packageManager: PackageManager): List<AppInfo> {
    val loadList = mutableListOf<AppInfo>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.queryIntentActivities(i, 0)
    for (ri in allApps) {
        val app = AppInfo()
        app.label = ri.loadLabel(packageManager)
        app.packageName = ri.activityInfo.packageName
        app.icon = ri.activityInfo.loadIcon(packageManager)
        loadList.add(app)
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
    return appsList
}

