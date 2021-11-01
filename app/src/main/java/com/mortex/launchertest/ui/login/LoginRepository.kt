package com.mortex.launchertest.ui.login

import com.mortex.launchertest.common.Utils.performGetOperation
import com.mortex.launchertest.local.SessionManager
import com.mortex.launchertest.ui.login.remote.LoginRemoteDataSource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val sessionManager: SessionManager
) {
    fun login(userName: String, pass: String) = performGetOperation {
        loginRemoteDataSource.login(
            userName, pass
        )
    }

    fun setToken(token: String) {
        sessionManager.setToken(token)
    }


}