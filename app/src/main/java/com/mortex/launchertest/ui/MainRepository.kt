package com.mortex.launchertest.ui

import androidx.lifecycle.LiveData
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.LauncherDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    val launcherDao: LauncherDao
) {

    suspend fun saveChildToDb(child: Child): Long {
        return launcherDao.insertChild(child)
    }

    fun getUnblockedApps(blocked: Boolean): LiveData<List<AppInfo>> {
        return launcherDao.getAllBlockedApps(blocked)
    }


    fun saveAllAppsToDb(list: List<AppInfo>) {
        CoroutineScope(IO).launch {
           launcherDao.insertAllApps(list)
        }
    }

    fun getAllApps(): LiveData<List<AppInfo>> {
       return  launcherDao.getAllApps()
    }

}