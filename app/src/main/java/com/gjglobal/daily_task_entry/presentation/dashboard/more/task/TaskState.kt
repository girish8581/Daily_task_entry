package com.gjglobal.daily_task_entry.presentation.dashboard.more.task

import com.gjglobal.daily_task_entry.domain.domain.model.project.ProjectData
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskCountData
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListItem

data class TaskState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskCreatedList: List<TaskListItem>? = null,
    var isTaskList: Boolean? = false,
    var isTaskSaved: Boolean? = false,
    var isStatusUpdated:Boolean? = false,
    var projectList:List<ProjectData>? = null,
    var isProjectList :Boolean? = false,
    var taskCountData :TaskCountData?=null,
    var isTaskCountData :Boolean? = false,
    var isTaskCountLoding :Boolean? = false,
    var isAddNewTask:Boolean?=false
)
