package com.mortex.launchertest.ui.login.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mortex.launchertest.network.Resource
import com.mortex.launchertest.ui.login.LoginRepository
import com.mortex.launchertest.ui.login.data.LoginResponse

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun login(userName: String, pass: String): LiveData<Resource<LoginResponse>> =
        loginRepository.login(userName, pass)

    fun setToken(token: String) {
        loginRepository.setToken(token)
    }


}

