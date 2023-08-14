package com.gjglobal.daily_task_entry.presentation.dashboard.home.tasklist

import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListItem

data class TaskListState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskList: List<TaskListItem>? = null,
    var isTaskList: Boolean? = false,
    var isStatusSaved: Boolean? = false,
    var isStatusUpdated:Boolean? = false
)