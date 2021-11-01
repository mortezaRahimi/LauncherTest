package com.mortex.launchertest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mortex.launchertest.ui.app_list.AppInfo
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.ui.app_list.AppInfoToShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var parentAppList: MutableLiveData<List<AppInfoToShow>> = MutableLiveData()
    private val _response = MutableLiveData<Long>()

    fun addUser(child: Child) {
        viewModelScope.launch(Dispatchers.IO) {
            _response.postValue(mainRepository.saveChildToDb(child))
        }
    }

    fun saveAllApps(list: List<AppInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.saveAllAppsToDb(list)
        }
    }

    private val _childAppList = MutableStateFlow<List<AppInfo>>(emptyList())
    val childAppList: StateFlow<List<AppInfo>> = _childAppList


    private fun doGetBlockedApps() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getApps(false)
                .catch { e ->
                }
                .collect {
                    _childAppList.value = it
                }
        }
    }

    init {
        doGetBlockedApps()
    }


    fun getAppsFrmDb(blocked: Boolean) {
        mainRepository.getFilteredAppsFromDb(blocked)
    }

    fun updateApp(appInfo: AppInfo) {
        mainRepository.updateApp(appInfo)
    }

}