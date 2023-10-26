package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.edittaskentry.EditTaskEntryRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.qatask.QaTaskRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa.RecentUpdateQaItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa.RecentUpdateQaRequest
import com.gjglobal.daily_task_entry.domain.domain.use_case.TaskListUseCase
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
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
    private val _recentUpdatesList = MutableStateFlow(listOf<RecentUpdateItem>())
    private val _recentUpdatesQaList = MutableStateFlow(listOf<RecentUpdateQaItem>())

    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()


    var recentUpdatesQaList = searchText
        .combine(_recentUpdatesQaList) { text, recentUpdatesList ->
            if (text.isBlank()) {
                recentUpdatesList
            } else {
                recentUpdatesList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _recentUpdatesQaList.value
        )


    var recentUpdatesList = searchText
        .combine(_recentUpdatesList) { text, recentUpdatesList ->
            if (text.isBlank()) {
                recentUpdatesList
            } else {
                recentUpdatesList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _recentUpdatesList.value
        )

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

    fun isEditTask(value:Boolean){
        _state.value=_state.value.copy(editData = value)
    }

    fun showEditSuccess(value:Boolean){
        _state.value=_state.value.copy(editDataSuccess = value)
    }

    fun editTaskItem(value:RecentUpdateItem){
        _state.value=_state.value.copy(recentUpdateItem = value)
    }
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
                                isTaskList = false,taskList = null
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
                        taskUpdateRequest= TaskUpdateRequest(task_status = taskStatusRequest.task_status, updated_date = currentDateApi(), entry_date = taskStatusRequest.date, task_name = taskStatusRequest.task_no),id= taskStatusRequest.id,onSuccess={}
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


    fun saveQaTaskStatus(qaTaskRequest: QaTaskRequest,onSuccess: () -> Unit) {
        taskListUseCase.saveQaTaskStatus(
            qaTaskRequest
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("QA status succuss",result.data.toString())
                    if (result.data != null) {
                        _state.value =
                            _state.value.copy(isLoading = false, isQaStatusSaved = true)

                    }
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

    fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest, id :String,onSuccess: () -> Unit) {
        taskListUseCase.updateTaskStatus(
            taskUpdateRequest = taskUpdateRequest,id = id
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                        _state.value =
                            _state.value.copy(isLoading = false, isStatusUpdated = true)

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

    fun getRecentUpdates(recentUpdateRequest: RecentUpdateRequest) {
        taskListUseCase.getRecentUpdates(recentUpdateRequest= recentUpdateRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, recentUpdatesList = result.data?.data,isRecentUpdatesList = true)
                            Log.e("result", result.data.toString())

                            _recentUpdatesList.update {
                                result.data?.data!!
                            }
                        } else {
                            _state.value = _state.value.copy(
                                isRecentUpdatesList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isRecentUpdatesList = false)
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

    fun getRecentQaUpdates(recentUpdateQaRequest: RecentUpdateQaRequest) {
        taskListUseCase.getRecentQaUpdates(recentUpdateQaRequest= recentUpdateQaRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isTaskList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, isRecentUpdatesQaList = true, recentUpdatesQaList = result.data?.data,isRecentUpdatesList = true)
                            Log.e("result", result.data.toString())

                            _recentUpdatesQaList.update {
                                result.data?.data!!
                            }
                        } else {
                            _state.value = _state.value.copy(
                                isRecentUpdatesQaList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isRecentUpdatesQaList = false)
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

    fun editTaskEntry(editTaskEntryRequest: EditTaskEntryRequest, id :String,onSuccess: () -> Unit) {
        taskListUseCase.editTaskEntry(
            editTaskEntryRequest, id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onSuccess.invoke()
                    Log.i("status succuss",result.data.toString())
                    _state.value =
                        _state.value.copy(isLoading = false, isStatusUpdated = true)

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

