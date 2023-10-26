package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
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
class ReportViewModel@Inject constructor(
    private val taskListUseCase: TaskListUseCase
) :ViewModel(){
    private val _state = mutableStateOf(ReportState())
    val state: State<ReportState> = _state

    private val _reportList = MutableStateFlow(listOf<RecentUpdateItem>())

    private var _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    var recentUpdatesList = searchText
        .combine(_reportList) { text, reportList ->
            if (text.isBlank()) {
                reportList
            } else {
                reportList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _reportList.value
        )

    fun getReports(recentUpdateRequest: RecentUpdateRequest) {
        taskListUseCase.getRecentUpdates(recentUpdateRequest= recentUpdateRequest)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value = _state.value.copy(isReportList = true)
                            _state.value =
                                _state.value.copy(isLoading = false, reportList = result.data?.data, isReportList = true)
                            Log.e("result", result.data.toString())

                            _reportList.update {
                                result.data?.data!!
                            }
                        } else {
                            _state.value = _state.value.copy(
                                isReportList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isReportList = false)
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