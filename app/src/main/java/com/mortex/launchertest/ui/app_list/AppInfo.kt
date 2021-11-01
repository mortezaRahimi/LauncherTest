package com.mortex.launchertest.ui.app_list

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class AppInfo(
    var label: String,
    @PrimaryKey
    var packageName: String,
    var blocked: Boolean
)