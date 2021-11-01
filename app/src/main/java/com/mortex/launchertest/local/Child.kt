package com.mortex.launchertest.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child")
data class Child(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
