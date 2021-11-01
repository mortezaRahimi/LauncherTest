package com.mortex.launchertest.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child")
data class Child(

    @PrimaryKey
    val id: Int,
    val name: String,
    val pass: String
)
