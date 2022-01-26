package com.mortex.launchertest.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class AppInfo(
    var label: String,
    @PrimaryKey
    var packageName: String,
    var blocked: Boolean,
    var icon:String,
    var forLinks:Boolean,
    var forOthers:Boolean
)