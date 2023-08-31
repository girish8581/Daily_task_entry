package com.gjglobal.daily_task_entry.presentation.dashboard.home.leave

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveData
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.LeaveSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val leaveSaveUseCase: LeaveSaveUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(LeaveState())
    val state: State<LeaveState> = _state

    private val _leaveList = MutableStateFlow(listOf<LeaveData>())
    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var leaveList = searchText
        .combine(_leaveList) { text, leaveList ->
            if (text.isBlank()) {
                leaveList
            } else {
                leaveList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _leaveList.value
        )

    fun getLeaveList(leaveListRequest: LeaveListRequest) {
        leaveSaveUseCase.getLeaveList(leaveListRequest = leaveListRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isLeaveList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, leaveList = result.data?.data)
                            _leaveList.update {
                                result.data?.data!!
                            }

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLeaveList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isLeaveList = false)
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

    fun saveLeave(leaveSaveRequest: LeaveSaveRequest,onSuccess: () -> Unit) {
        leaveSaveUseCase.saveLeave(
            leaveSaveRequest = leaveSaveRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isLeaveSaved = true)

                    }
                }

                is Resource.Error -> {
                    Log.i("status error",result.message.toString())
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred",
                    )

                }

                is Resource.Loading -> {
                    Log.e("loading", "")
                    _state.value = _state.value.copy(isLoading = true)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

}

