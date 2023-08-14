package com.gjglobal.daily_task_entry.presentation.dashboard.home.tasklist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.TaskListUseCase
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
class TaskListViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(TaskListState())
    val state: State<TaskListState> = _state

    private val _taskList = MutableStateFlow(listOf<TaskListItem>())
    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var taskList = searchText
        .combine(_taskList) { text, taskList ->
            if (text.isBlank()) {
                taskList
            } else {
                taskList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _taskList.value
        )

    fun getTaskList(taskListRequest: TaskListRequest) {
        taskListUseCase.getTaskList(taskListRequest = taskListRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, taskList = result.data?.data)
                            _taskList.update {
                                result.data?.data!!
                            }

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isTaskList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isTaskList = false)
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

    fun saveTaskStatus(taskStatusRequest: TaskStatusRequest,onSuccess: () -> Unit) {
        taskListUseCase.saveTaskStatus(
            taskStatusRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isStatusSaved = true)

                    }
                    // updating task status to staffTaskMapping table
                    updateTaskStatus(
                        taskUpdateRequest= TaskUpdateRequest(task_status = taskStatusRequest.task_status),id= taskStatusRequest.id
                    )

                    //get updated in progress list
                    //getTaskList(taskListRequest = TaskListRequest(staff_name = taskStatusRequest.staff_name))
                }

                is Resource.Error -> {
                    Log.i("status error",result.data.toString())
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

    private fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest, id :String) {
        taskListUseCase.updateTaskStatus(
            taskUpdateRequest = taskUpdateRequest,id = id
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.i("status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isStatusUpdated = true)

                    }
                }

                is Resource.Error -> {
                    Log.i("status error",result.data.toString())
                    _state.value = _state.value.copy(
                        isLoading = false,isStatusUpdated = false,
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

