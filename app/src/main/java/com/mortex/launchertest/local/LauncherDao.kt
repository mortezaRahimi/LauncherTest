package com.mortex.launchertest.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LauncherDao {

    @Query("SELECT * FROM child")
    fun getAllChildren(): LiveData<List<Child>>

    @Query("SELECT * FROM child WHERE id = :Id")
    fun getChild(Id: Int): LiveData<Child>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChildren(children: List<Child>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: Child)


}