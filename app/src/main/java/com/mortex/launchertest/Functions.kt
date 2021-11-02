package com.mortex.launchertest

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.network.Resource
import com.mortex.launchertest.local.AppInfo
import kotlinx.coroutines.Dispatchers

val appsList: MutableList<AppInfo> = ArrayList()

fun loadApps(packageManager: PackageManager): List<AppInfo> {
    val loadList = mutableListOf<AppInfo>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.queryIntentActivities(i, 0)
    for (ri in allApps) {
        val app = AppInfo(
            ri.loadLabel(packageManager).toString(),
            ri.activityInfo.packageName,
            false,
            (ri.activityInfo.loadIcon(packageManager)).toString()
        )

        loadList.add(app)
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
    return appsList
}

fun <T> performGetOperation(
    networkCall: suspend () -> Resource<T>
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Resource.Status.SUCCESS) {

            emit(Resource.success(responseStatus.data) as Resource<T>)

        } else if (responseStatus.status == Resource.Status.ERROR) {
            emit(Resource.error(responseStatus.message!!))

        }
    }

