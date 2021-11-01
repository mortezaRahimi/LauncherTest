package com.mortex.launchertest

import android.content.Intent
import android.content.pm.PackageManager
import com.mortex.launchertest.ui.app_list.AppInfoToShow

val appsList: MutableList<AppInfoToShow> = ArrayList()

fun loadApps(packageManager: PackageManager): List<AppInfoToShow> {
    val loadList = mutableListOf<AppInfoToShow>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.queryIntentActivities(i, 0)
    for (ri in allApps) {
        val app = AppInfoToShow(
            ri.loadLabel(packageManager),
            ri.activityInfo.packageName,
            false,
            ri.activityInfo.loadIcon(packageManager)
        )
        loadList.add(app)
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
    return appsList
}

