package com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata

data class TaskDetails(
    val created_on: String,
    val id: String,
    val project_code: String,
    val project_id: String,
    val project_name: String,
    val task_details: String,
    val task_end_date: Any,
    val task_estimate_date: String,
    val task_jira_no: String,
    val task_name: String,
    val task_no: String,
    val task_start_date: String,
    val task_status: String
)