package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskStatusRequest(
    val assigned_date: String,
    val created_by: String,
    val created_on: String,
    val date: String,
    val day_type: String,
    val end_time: String,
    val jira_no: String,
    val job_done: String,
    val leave_details: String,
    val leave_status: Int,
    val project_name: String,
    val project_status: String,
    val remarks_any: String,
    val session_type: String,
    val staff_name: String,
    val start_time: String,
    val task_details: String,
    val task_no: String,
    val task_status: String,
    val work_at: String,
    val id :String,
    val completed_level:String,
    val break_hours:String
)