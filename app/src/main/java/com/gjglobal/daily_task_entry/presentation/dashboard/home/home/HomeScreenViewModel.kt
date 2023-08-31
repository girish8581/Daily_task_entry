package com.gjglobal.daily_task_entry.presentation.dashboard.home.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.TaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase
) :ViewModel(){

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    fun gatTasksCount(taskCountSummaryRequest: taskCountSummaryRequest) {
        Log.e("request",taskCountSummaryRequest.toString())
        taskListUseCase.getTasksCount(taskCountSummaryRequest = taskCountSummaryRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value =
                                _state.value.copy(isLoading = false, isTaskCount = true, taskCount = result.data?.data)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isTaskCount = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isTaskCount = false)
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