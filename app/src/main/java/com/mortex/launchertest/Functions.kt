package com.mortex.launchertest

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.network.Resource
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import kotlinx.coroutines.Dispatchers

fun loadApps(packageManager: PackageManager): List<AppInfoWithIcon> {
    val loadList = mutableListOf<AppInfoWithIcon>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.queryIntentActivities(i, 0)
    for (ri in allApps) {
        val app = AppInfoWithIcon(
            ri.loadLabel(packageManager).toString(),
            ri.activityInfo.packageName,
            false,
            ri.activityInfo.loadIcon(packageManager),
            forOthers = false
        )

        loadList.add(app)
    }
    loadList.sortBy { it.label }

    return loadList
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


