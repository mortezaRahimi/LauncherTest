package com.mortex.launchertest.ui.login.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.network.Resource
import com.mortex.launchertest.ui.login.LoginRepository
import com.mortex.launchertest.ui.login.data.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun login(userName: String, pass: String): LiveData<Resource<LoginResponse>> =
        loginRepository.login(userName, pass)

    fun setToken(token: String) {
        loginRepository.setToken(token)
    }

    val childList = loginRepository.getChildrenFromDb()

    private val _userDetails = MutableStateFlow<List<Child>>(emptyList())
    val userDetails: StateFlow<List<Child>> = _userDetails

init {
    doGetUserDetails()
}
    private fun doGetUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.getUserDetails
                .catch { e ->
                    //Log error here
                }
                .collect {
                    _userDetails.value = it
                }
        }
    }

}

