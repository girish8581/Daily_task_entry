package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskMasterData(
    val id: String,
    val project_name: String,
    val task_details: String,
    val task_estimate_date: String,
    val task_name: String,
    val task_start_date: String,
    val task_status: String
)