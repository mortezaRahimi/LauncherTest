package com.mortex.launchertest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.Child
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var parentAppList: MutableLiveData<List<AppInfo>> = MutableLiveData()
    private val _response = MutableLiveData<Long>()

    fun addUser(child: Child) {
        viewModelScope.launch(Dispatchers.IO) {
            _response.postValue(mainRepository.saveChildToDb(child))
        }
    }

    fun saveAllApps(list: List<AppInfo>) {
        parentAppList.value = list
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.saveAllAppsToDb(list)
        }
    }

    var childAppList = MutableLiveData<List<AppInfo>>(emptyList())

    fun doGetUnblockedApps(): LiveData<List<AppInfo>> {
        return mainRepository.getUnblockedApps(false)
    }


    fun getAllApps(): LiveData<List<AppInfo>> {
        return mainRepository.getAllApps()
    }


}