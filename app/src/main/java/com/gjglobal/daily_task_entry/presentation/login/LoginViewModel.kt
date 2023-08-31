package com.gjglobal.daily_task_entry.presentation.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.LoginUseCase
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val cacheManager: CacheManager,
    private val loginUseCase: LoginUseCase

) : ViewModel(){
    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    fun isValidUsername(username:String){
        _state.value=_state.value.copy(userName = username, isValidUserName = username.isNotEmpty())
    }
    fun isValidEmailId(email:String){
        _state.value=_state.value.copy(emailId = email, isValidEmailId = email.isNotEmpty())
    }
    fun isValidPassword(password:String){
        _state.value=_state.value.copy(password=password, isValidPassword = password.isNotEmpty())
    }

    fun isValidNewPassword(password:String){
        _state.value=_state.value.copy(newPassword = password, isValidPassword = password.isNotEmpty())
    }

    fun isValidConfirmPassword(password:String){
        _state.value=_state.value.copy(confirmPassword = password, isValidPassword = password.isNotEmpty())
    }
    fun login(username: String,password: String,activity: Activity) {
        //cacheManager.saveUsernamePassword(unamePwd = LoginRequest(username= username, password = password))
        loginUseCase.login(LoginRequest(username =username, password = password)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.e("login log", result.data.toString())
                    _state.value = _state.value.copy(isLoading = false, authorization = result.data)
                    result.data?.let { cacheManager.saveAuthResponse(it) }

//                    activity.startActivity(
//                        Intent(
//                            activity,
//                            DashboardActivity::class.java
//                        )
//                    )
                    activity.finish()
                }

                is Resource.Error -> {
                        Log.e("error login", result.data.toString())

                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}