package com.mortex.launchertest.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.local.LauncherDao
import com.mortex.launchertest.local.SessionManager
import com.mortex.launchertest.performGetOperation
import com.mortex.launchertest.ui.login.remote.LoginRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val sessionManager: SessionManager,
    private val launcherDao: LauncherDao
) {
    fun login(userName: String, pass: String) = performGetOperation {
        loginRemoteDataSource.login(
            userName, pass
        )
    }

    fun setToken(token: String) {
        sessionManager.setToken(token)
    }

    val getUserDetails: Flow<List<Child>> get() =  launcherDao.getAllChildren()
}