package com.mortex.launchertest.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link")
data class ULinks(
    val name: String, val url: String,
    @PrimaryKey
    var id: Int,
)