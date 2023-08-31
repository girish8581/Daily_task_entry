package com.gjglobal.daily_task_entry.presentation.dashboard.home.home

import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.TaskSummaryCountData

data class HomeScreenState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskCount: List<TaskSummaryCountData>? = null,
    var isTaskCount:Boolean?=false
)