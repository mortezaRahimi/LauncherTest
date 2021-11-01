package com.mortex.launchertest.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mortex.launchertest.ui.app_list.AppInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LauncherDao {

    @Query("SELECT * FROM child")
    fun getAllChildren(): Flow<List<Child>>

    @Query("SELECT * FROM child WHERE id = :Id")
    fun getChild(Id: Int): LiveData<Child>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChildren(children: List<Child>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: Child):Long


//    @Query("SELECT * FROM app_info")
//    fun getAllApps(): LiveData<List<AppInfo>>

    @Query("SELECT * FROM app_info WHERE blocked = :blockedItem ")
    fun getAllBlockedApps(blockedItem: Boolean): LiveData<List<AppInfo>>

//    @Query("SELECT * FROM app_info WHERE Id = :Id")
//    fun getApp(Id: Int): LiveData<Child>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllApps(apps: List<AppInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(appInfo: AppInfo)


}