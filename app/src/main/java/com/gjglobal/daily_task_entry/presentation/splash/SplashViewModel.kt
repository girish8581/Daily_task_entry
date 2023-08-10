package com.gjglobal.daily_task_entry.presentation.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.hms_gj.presentation.splash.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(SplashState())
    val state: State<SplashState> = _state
    init {
        _state.value = SplashState(isLoading = true)
        viewModelScope.launch {
            delay(1000)
            _state.value = SplashState(isLoading = false)
        }
    }
}