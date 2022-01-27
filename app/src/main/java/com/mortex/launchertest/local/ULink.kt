package com.mortex.launchertest.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link")
data class ULink(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var url: String
)