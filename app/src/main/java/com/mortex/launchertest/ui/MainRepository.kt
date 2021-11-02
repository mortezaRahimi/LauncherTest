package com.mortex.launchertest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.ui.app_list.AppInfo
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.LauncherDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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


    fun getUnblockedApps(blocked: Boolean): LiveData<List<AppInfo>> {
        return launcherDao.getAllBlockedApps(blocked)
    }

//    fun getAllApps(): Flow<List<AppInfo>> {
//        return launcherDao.getAllApps()
//    }

//    suspend fun saveAllAppsToDb(apps: List<AppInfo>) {
//        return launcherDao.insertAllApps(apps)
//    }

    fun saveAllAppsToDb(list: List<AppInfo>) {
        CoroutineScope(IO).launch {
           launcherDao.insertAllApps(list)
        }
    }

    fun getAllApps(): LiveData<List<AppInfo>> {
       return  launcherDao.getAllApps()
    }

}