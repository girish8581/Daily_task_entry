package com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise

data class TaskDataDateWise(
    val assigned_date: String,
    val date: String,
    val end_time: String,
    val id: String,
    val jira_no: String,
    val job_done: String,
    val project_name: String,
    val staff_name: String,
    val start_time: String,
    val taskTime: String,
    val task_details: String,
    val task_id: String,
    val task_no: String,
    val task_status: String,
    val timeTaken: String,
    val updated_date: String
)