package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.use_case.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel  @Inject constructor(
    private val useCase: NotificationUseCase,
    cacheManager: CacheManager
):ViewModel() {

    private val _state = mutableStateOf(NotificationScreenState())
    val state: State<NotificationScreenState> = _state

    fun getNotification() {
        useCase.getNotification()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value =
                                _state.value.copy(isLoading = false, isNotificationList = true, notificationList = result.data?.data)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isNotificationList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isNotificationList = false)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }
}