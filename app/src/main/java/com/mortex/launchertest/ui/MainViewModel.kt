package com.mortex.launchertest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.ULink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var parentAppList: MutableLiveData<ArrayList<AppInfo>> = MutableLiveData()
    var parentAppWithIconList: MutableLiveData<List<AppInfoWithIcon>> = MutableLiveData()

    private val _userDetails = MutableStateFlow<List<Child>>(emptyList())
    val userDetails: StateFlow<List<Child>> = _userDetails

    private val _response = MutableLiveData<Long>()

    fun removeLinks() {
        viewModelScope.launch {
            mainRepository.removeAllLinks()
        }
    }

    fun getAllLinks(): LiveData<List<ULink>> {
        return mainRepository.getAllLinks()
    }

    fun saveALLLinks(list: List<ULink>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.saveAllLinks(list)
        }
    }

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

    fun saveAllApps(list: ArrayList<AppInfo>) {

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

    fun getMachinePath(): String? = mainRepository.getMachinePath()

    fun saveMachinePath(value:String){
        mainRepository.saveMachinePath(value)
    }


}