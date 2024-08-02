package com.gjglobal.daily_task_entry.domain.domain.model.requestmodel

data class TaskListRequest(
    val staff_name: String,
    val task_status :String
)


data class TaskListRequestNew(
    val staff_name: String,
    val task_status :String,
    val staff_type :String,
    val project_name:String
)