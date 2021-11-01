package com.mortex.launchertest.ui.app_list

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

data class AppInfoToShow(
    var label: CharSequence,
    var packageName: CharSequence,
    var blocked: Boolean,
    var icon: Drawable,
)