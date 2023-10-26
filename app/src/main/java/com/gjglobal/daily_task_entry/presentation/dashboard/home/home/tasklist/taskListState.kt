package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist

import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa.RecentUpdateQaItem

data class TaskListState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskList: List<TaskListItem>? = null,
    var recentUpdatesList: List<RecentUpdateItem>? = null,
    var recentUpdatesQaList: List<RecentUpdateQaItem>? = null,
    var isRecentUpdatesList: Boolean? = false,
    var isRecentUpdatesQaList: Boolean? = false,
    var isTaskList: Boolean? = false,
    var isStatusSaved: Boolean? = false,
    var isQaStatusSaved: Boolean? = false,
    var isStatusUpdated:Boolean? = false,
    var recentUpdateItem :RecentUpdateItem? = null,
    var editData :Boolean? = false,
    var editDataSuccess :Boolean? = false
)