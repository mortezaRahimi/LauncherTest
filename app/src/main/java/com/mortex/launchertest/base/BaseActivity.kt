package com.mortex.launchertest.base

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle??) {
        super.onCreate(savedInstanceState)

    }

    protected abstract fun onKeyboardOpen()
    protected abstract fun onKeyboardClosed()

    fun showMessage(s:String){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }

     fun calculateAvatarSize(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        return ((height * 4) / 100) * 2
    }
}