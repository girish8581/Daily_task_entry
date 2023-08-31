package com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign

import com.gjglobal.daily_task_entry.domain.domain.model.staff.StaffData
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMasterData
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.TaskDetails

data class TaskAssignState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskCreatedList: List<TaskListItem>? = null,
    var staffList: List<StaffData>? = null,
    var taskList: List<TaskMasterData>? = null,
    var isTaskMapList: Boolean? = false,
    var isTaskMapSaved: Boolean? = false,
    var isStaffList :Boolean?=false,
    var isTaskList :Boolean?=false,
    var taskData:TaskDetails?=null,
    var isTaskData:Boolean?=false,
    var isTaskLoading:Boolean?=false,
    var taskTime:String? = null,
    var isValidTaskTime:Boolean?=false,
    var isAlreadyAssigned :Boolean?=false
)
