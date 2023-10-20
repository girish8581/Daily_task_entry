package com.gjglobal.daily_task_entry.presentation.dashboard.more.task

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.project.ProjectData
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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(TaskState())
    val state: State<TaskState> = _state

    private val _projectList = MutableStateFlow(listOf<ProjectData>())
    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var taskList = searchText
        .combine(_projectList) { text, taskList ->
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
            _projectList.value
        )

    fun getProjects() {
        taskListUseCase.getProjects()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isProjectList = true,projectList = result.data?.data)
                            _projectList.update {
                                result.data?.data!!
                            }

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,isProjectList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isProjectList = false)
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


    fun getTaskCount(project_name :String) {
        taskListUseCase.getTaskCount(project_name = project_name)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isTaskCountLoding = false, taskCountData = result.data?.data,isTaskCountData=true)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isTaskCountData=true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isTaskCountData = false)
                        _state.value = _state.value.copy(
                            isTaskCountLoding = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isTaskCountLoding = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }


    fun addNewTask(addNewTaskRequest: AddNewTaskRequest,onSuccess: () -> Unit) {
        taskListUseCase.addNewTask(
            addNewTaskRequest = addNewTaskRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isAddNewTask = true)
                    }
                }

                is Resource.Error -> {
                    Log.i("status error",result.data.toString())
                    _state.value = _state.value.copy(
                        isLoading = false,isAddNewTask=false,
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