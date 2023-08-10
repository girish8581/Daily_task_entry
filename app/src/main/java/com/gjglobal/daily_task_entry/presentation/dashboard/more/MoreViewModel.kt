package com.gjglobal.daily_task_entry.presentation.dashboard.more

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.presentation.dashboard.more.MoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val cacheManager: CacheManager,
) : ViewModel() {

    private val _state = mutableStateOf(MoreState())
    val state: State<MoreState> = _state

//
//    fun logout(activity: Activity) {
//        logoutUseCase.logout(userName = cacheManager.getUsernamePassword()?.username!!)
//            .onEach { result ->
//                when (result) {
//                    is Resource.Success -> {
//                        _state.value =
//                            _state.value.copy(isLoading = false, logoutData = result.data)
//                        cacheManager.saveUsernamePassword(LoginRequest(username = "",password = ""))
//                        val intent = Intent(activity, MainActivity::class.java)
//                        activity.startActivity(intent)
//                        activity.finish()
//                    }
//
//                    is Resource.Error -> {
//                        _state.value = _state.value.copy(
//                            isLoading = false,
//                            error = result.message ?: "An unexpected error occurred"
//                        )
//                    }
//
//                    is Resource.Loading -> {
//                        _state.value = _state.value.copy(isLoading = true)
//                    }
//
//                    else -> {}
//                }
//            }.launchIn(viewModelScope)
//    }
}