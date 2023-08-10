package com.gjglobal.daily_task_entry.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val cacheManager: CacheManager
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
//    fun login(username: String,password: String,activity:Activity) {
//        cacheManager.saveUsernamePassword(unamePwd = LoginRequest(username= username, password = password))
//        loginUseCase.login(LoginRequest(username =username, password = password)).onEach { result ->
//            when (result) {
//                is Resource.Success -> {
//                    Log.e("1st log", result.data.toString())
//                    Log.e("1st log", result.data?.message.toString())
//                    Log.e("1st log", result.data?.success.toString())
//                    Log.e("1st log", result.data?.status.toString())
//                    Log.e("1st log", result.data?.data.toString())
//                    _state.value = _state.value.copy(isLoading = false, authorization = result.data)
//                    result.data?.let { cacheManager.saveAuthResponse(it) }
//                }
//                is Resource.Error -> {
//                    if(result.message == "HTTP 409 "){
//                        Log.e("2nd log", result.data.toString())
//                      if (  cacheManager.getUsernamePassword()?.username!!.isNullOrEmpty().not()){
//                          Log.e("3rd log", result.data.toString())
//                          _state.value = _state.value.copy(isLogout = true)
//                      }else{
//                          Log.e("4th log", result.data.toString())
//                          activity.startActivity(
//                              Intent(
//                                  activity,
//                                  DashboardActivity::class.java
//                              )
//                          )
//                          activity.finish()
//                      }
//
//                    }else if (result.message == "HTTP 400 "){
//                        Toast.makeText(activity,"Invalid Credentials",Toast.LENGTH_SHORT).show()
//                    }
//                   Log.e("5th log", result.data?.success.toString())
//                        Log.e("6th log", result.data.toString())
//
//                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "An unexpected error occurred")
//                }
//                is Resource.Loading -> {
//                    _state.value = _state.value.copy(isLoading = true)
//                }
//
//                else -> {}
//            }
//        }.launchIn(viewModelScope)
//    }
}