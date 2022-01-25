package com.mortex.launchertest.local

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey


data class AppInfoWithIcon(
    var label: String,
    var packageName: String,
    var blocked: Boolean,
    var icon:Drawable
)