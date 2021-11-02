package com.mortex.launchertest.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import java.util.*
import android.view.animation.TranslateAnimation
import android.R
import android.animation.ObjectAnimator
import android.view.animation.Animation

import android.view.animation.LinearInterpolator

import android.view.animation.RotateAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.network.Resource
import kotlinx.coroutines.Dispatchers


object Utils {

    fun Fragment.showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}


