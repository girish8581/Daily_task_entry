package com.gjglobal.daily_task_entry.presentation.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    cacheManager: CacheManager
) : ViewModel() {

    private val _state = mutableStateOf(DashboardScreenState())
    val state: State<DashboardScreenState> = _state

    init {
        _state.value = _state.value.copy(authorization = cacheManager.getAuthResponse())
    }

    fun hideBottomMenu(boolean: Boolean) {
        _state.value = _state.value.copy(hideBottomMenu = boolean)
    }
}