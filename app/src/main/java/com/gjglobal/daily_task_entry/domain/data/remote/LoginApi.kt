package com.gjglobal.daily_task_entry.domain.data.remote

import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("services/getLogin.php")
    suspend fun login(@Body loginRequest: LoginRequest): Authorization
}