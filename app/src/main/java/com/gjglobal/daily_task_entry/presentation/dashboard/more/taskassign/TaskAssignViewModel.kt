package com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.project.ProjectData
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMappingRequest
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
class TaskAssignViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase,
) : ViewModel(){

    private val _state = mutableStateOf(TaskAssignState())
    val state: State<TaskAssignState> = _state

    private val _taskMapList = MutableStateFlow(listOf<ProjectData>())
    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var taskMapList = searchText
        .combine(_taskMapList) { text, taskMapList ->
            if (text.isBlank()) {
                taskMapList
            } else {
                taskMapList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _taskMapList.value
        )

    fun isValidTaskTime(hrs:String){
        _state.value=_state.value.copy(taskTime = hrs, isValidTaskTime = hrs.isNotEmpty())
    }


    fun getTasksProjectName(project_name:String) {
        taskListUseCase.getTasksProjectName(project_name=project_name)

            .onEach { result ->
                when (result) {

                    is Resource.Success -> {
                        Log.e("tasks",result.data.toString())
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isTaskLoading = false, isTaskList = true, taskList = result.data?.data)

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isTaskLoading = false,isTaskList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isStaffList = false)
                        _state.value = _state.value.copy(
                            isTaskLoading = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isTaskLoading = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

//    suspend fun getStaffTaskDateWise(staffTaskDateWiseRequest: StaffTaskDateWiseRequest): StaffTaskDateWiseResponse
    fun getStaffTaskDateWise(staffTaskDateWiseRequest: StaffTaskDateWiseRequest) {
        taskListUseCase.getStaffTaskDateWise(staffTaskDateWiseRequest=staffTaskDateWiseRequest)
            .onEach { result ->
                when (result) {

                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isTaskData = true, staffTaskDateWise = result.data?.data)

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,isTaskData = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isTaskData = false)
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


    fun getTaskData(taskName:String) {
        taskListUseCase.getTaskData(taskName=taskName)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isTaskData = true, taskData = result.data?.data)

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,isTaskData = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isTaskData = false)
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


    fun getTasks() {
        taskListUseCase.getTasks()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isTaskList = true, taskList = result.data?.data)

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,isTaskList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isStaffList = false)
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

    fun getStaffs() {
        taskListUseCase.getStaffs()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isStaffList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isStaffList = true, staffList = result.data?.data)

                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,isStaffList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isStaffList = false)
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

    fun addTaskAssign(taskMappingRequest: TaskMappingRequest, onSuccess: () -> Unit) {
        taskListUseCase.addTaskMap(
            taskMappingRequest = taskMappingRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.e("status code", result.data!!.status.toString())

                    if (result.data.status == 201)
                    {
                        _state.value =
                            _state.value.copy(isLoading = false, isTaskMapSaved = true,isAlreadyAssigned = false)
                    }
                }

                is Resource.Error -> {
                    if(result.message == "HTTP 409 Exist"){
                        _state.value = _state.value.copy(
                            isLoading = false, isTaskMapSaved =false,isAlreadyAssigned = true,
                            error = result.message ?: "An unexpected error occurred",
                        )
                    }
                    Log.i("status error",result.message.toString())
                    _state.value = _state.value.copy(
                        isLoading = false, isTaskMapSaved =false,
                        error = result.message ?: "An unexpected error occurred",
                    )
                }

                is Resource.Loading -> {
                    Log.e("loading", "")
                    _state.value = _state.value.copy(isLoading = true,isAlreadyAssigned = false)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


}