package com.gjglobal.daily_task_entry.presentation.dashboard.home.otherjobs

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveData
import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.AddNewOtherJobRequest
import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.OtherJobData
import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.StaffName
import com.gjglobal.daily_task_entry.domain.domain.model.task.AddNewTaskRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.TaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OtherJobViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(OtherJobState())
    val state: State<OtherJobState> = _state

    private val _otherJobList = MutableStateFlow(listOf<OtherJobData>())
    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var otherJobList = searchText
        .combine(_otherJobList) { text, otherJobList ->
            if (text.isBlank()) {
                otherJobList
            } else {
                otherJobList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _otherJobList.value
        )


    fun addNewOtherJob(addNewOtherJobRequest: AddNewOtherJobRequest, onSuccess: () -> Unit) {
        taskListUseCase.addNewOtherJob(
            addNewOtherJobRequest = addNewOtherJobRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isOtherJobList = true)
                    }
                }

                is Resource.Error -> {
                    Log.i("status error",result.data.toString())
                    _state.value = _state.value.copy(
                        isLoading = false,isOtherJobList=false,
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




    fun getOtherJob(staffName: String) {
        taskListUseCase.getOtherJob(staffName = staffName)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isOtherJobList = true)
                            _state.value =
                                _state.value.copy(isLoading  = false, otherJobList = result.data?.data, isOtherJobList =true)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isOtherJobList=true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isOtherJobList = false)
                        _state.value = _state.value.copy(
                            isOtherJobList = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isOtherJobList = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }





}