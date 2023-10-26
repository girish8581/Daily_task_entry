package com.gjglobal.daily_task_entry.domain.domain.model.task.edittaskentry

data class EditTaskEntryRequest(
    val break_hours: String,
    val completed_level: String,
    val created_on: String,
    val date: String,
    val end_time: String,
    val id: String,
    val jira_no: String,
    val job_done: String,
    val staff_name: String,
    val start_time: String,
    val task_details: String,
    val task_no: String,
    val task_status: String
)