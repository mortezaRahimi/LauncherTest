package com.mortex.launchertest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.ui.app_list.AppInfo
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.LauncherDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    val launcherDao: LauncherDao
) {

//    suspend fun saveChildToDb(child: Child): Long =
//        liveData(Dispatchers.IO) {
//            launcherDao.insertChild(child)
//        }

    suspend fun saveChildToDb(child: Child): Long {
        return launcherDao.insertChild(child)
    }


    suspend fun saveAllAppsToDb(apps: List<AppInfo>) {
        return launcherDao.insertAllApps(apps)
    }
//    fun saveAllAppsToDb(apps: List<AppInfo>): LiveData<Void> = liveData(Dispatchers.IO) {
//        launcherDao.insertAllApps(apps)
//    }

//    fun getAllAppsFromDb(): LiveData<List<AppInfo>> = liveData(Dispatchers.IO) {
//        launcherDao.getAllApps()
//    }

    fun getApps(blocked: Boolean): Flow<List<AppInfo>> {
        return launcherDao.getAllBlockedApps(blocked)
    }

    fun getFilteredAppsFromDb(blocked: Boolean): LiveData<List<AppInfo>> =
        liveData(Dispatchers.IO) {
            launcherDao.getAllBlockedApps(blocked)
        }

    fun updateApp(app: AppInfo): LiveData<Void> = liveData(Dispatchers.IO) {
        launcherDao.insertApp(app)
    }

}