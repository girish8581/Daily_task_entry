package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskMappingRequest(
    val assigned_date: String,
    val project_id: String,
    val project_name: String,
    val staff_id: String,
    val staff_name: String,
    val task_id: String,
    val task_name: String,
    val task_status: String,
    val updated_date:String,
    val task_details:String,
    val taskTime:String
)