package com.gjglobal.daily_task_entry.presentation.login

import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization


data class LoginState(
    var userName:String="",
    var password:String="",
    var emailId:String="",
    var newPassword:String="",
    var confirmPassword:String="",
    var isLoading: Boolean = false,
    var error: String? = null,
    var isValidUserName:Boolean=false,
    var loginDateTime:String="",
    var isValidPassword:Boolean=false,
    val authorization: Authorization? = null,
    val loginStatus :Boolean? = null,
    var isLogout : Boolean = false,
    var isPasswordChanged:Boolean = false,
    var isValidEmailId:Boolean=false,
)
