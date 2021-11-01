package com.mortex.launchertest.ui.login.remote

import com.mortex.launchertest.network.BaseDataSource
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginService: LoginService
) : BaseDataSource() {

    suspend fun login(username: String, pass: String) =
        getResult { loginService.login(username, pass) }


}