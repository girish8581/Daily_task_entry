package com.gjglobal.daily_task_entry.domain.data.repository.login

import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest


interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Authorization
}