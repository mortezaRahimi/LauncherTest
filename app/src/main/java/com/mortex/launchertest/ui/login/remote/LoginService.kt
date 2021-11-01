package com.mortex.launchertest.ui.login.remote

import com.mortex.launchertest.network.URLConstants.LOGIN
import com.mortex.launchertest.ui.login.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    @POST(LOGIN)
    suspend fun login(
        @Query("username") userName: String,
        @Query("password") pass: String
    ): Response<LoginResponse>


}