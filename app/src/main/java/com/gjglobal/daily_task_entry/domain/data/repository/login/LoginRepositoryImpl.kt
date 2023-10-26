package com.gjglobal.daily_task_entry.domain.data.repository.login

import com.gjglobal.daily_task_entry.domain.data.remote.LoginApi
import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api:LoginApi
):LoginRepository{
    override suspend fun login(loginRequest: LoginRequest): Authorization {
        return api.login(loginRequest = loginRequest)
    }
}