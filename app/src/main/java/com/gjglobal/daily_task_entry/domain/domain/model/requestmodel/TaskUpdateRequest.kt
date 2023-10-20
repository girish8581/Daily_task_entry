package com.gjglobal.daily_task_entry.domain.domain.model.requestmodel

data class TaskUpdateRequest(
    val task_status: String,
    val updated_date:String,
    val entry_date:String,
    val task_name:String
)