package com.mortex.launchertest.ui

import androidx.lifecycle.LiveData
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.LauncherDao
import com.mortex.launchertest.local.ULink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    val launcherDao: LauncherDao
) {

    suspend fun saveAllLinks(links: List<ULink>) {
        launcherDao.insertAllLinks(links)
    }

    fun getAllLinks(): LiveData<List<ULink>> {
        return launcherDao.getAllLinks()
    }

    suspend fun removeAllLinks() {
        launcherDao.removeLinks()
    }

    suspend fun saveChildToDb(child: Child): Long {
        return launcherDao.insertChild(child)
    }

    suspend fun deleteChild() {
        launcherDao.removeChild()
    }

    fun getUnblockedApps(
        blocked: Boolean
    ): LiveData<List<AppInfo>> {
        return launcherDao.getAllBlockedApps(blocked)
    }


    fun getForOthersApps(
        forOthers: Boolean
    ): LiveData<List<AppInfo>> {
        return launcherDao.getAllLOthersApps(forOthers)
    }

    fun saveAllAppsToDb(list: List<AppInfo>) {
        CoroutineScope(IO).launch {
            launcherDao.insertAllApps(list)
        }
    }

    fun getAllApps(): LiveData<List<AppInfo>> {
        return launcherDao.getAllApps()
    }

    val getUserDetails: Flow<List<Child>> get() = launcherDao.getAllChildren()

}