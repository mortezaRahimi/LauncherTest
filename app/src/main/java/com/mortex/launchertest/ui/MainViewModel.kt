package com.mortex.launchertest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.local.Child
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var parentAppList: MutableLiveData<List<AppInfo>> = MutableLiveData()
    var parentAppWithIconList: MutableLiveData<List<AppInfoWithIcon>> = MutableLiveData()

    private val _userDetails = MutableStateFlow<List<Child>>(emptyList())
    val userDetails: StateFlow<List<Child>> = _userDetails


    private val _response = MutableLiveData<Long>()

    fun removeChild() {
        viewModelScope.launch {
            mainRepository.deleteChild()
        }
    }

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

    fun doGetUnblockedApps(
        blocked: Boolean
    ): LiveData<List<AppInfo>> {
        return mainRepository.getUnblockedApps(blocked)
    }

    fun doGetLinksApps(
        links: Boolean
    ): LiveData<List<AppInfo>> {
        return mainRepository.getForLinksApps(links)
    }

    fun doGetOthersApps(
        others: Boolean
    ): LiveData<List<AppInfo>> {
        return mainRepository.getForOthersApps(others)
    }


    fun getAllApps(): LiveData<List<AppInfo>> {
        return mainRepository.getAllApps()
    }

    private fun doGetUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getUserDetails
                .catch { e ->
                    //Log error here
                }
                .collect {
                    _userDetails.value = it
                }
        }
    }


}