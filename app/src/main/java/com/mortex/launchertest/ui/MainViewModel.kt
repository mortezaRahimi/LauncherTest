package com.mortex.launchertest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mortex.launchertest.AppInfo

class MainViewModel(): ViewModel() {

    var appList: MutableLiveData<List<AppInfo>> = MutableLiveData()
}