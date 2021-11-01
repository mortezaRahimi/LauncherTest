package com.mortex.launchertest.local

import android.content.SharedPreferences
import com.mortex.launchertest.local.Constants.TOKEN
import javax.inject.Inject

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {

    fun getToken() = preferences.getString(TOKEN, null)

    fun setToken(value: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, "Bearer $value")
        editor.apply()
    }



}