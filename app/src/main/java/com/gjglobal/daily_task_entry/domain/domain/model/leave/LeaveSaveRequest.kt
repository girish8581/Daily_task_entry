package com.gjglobal.daily_task_entry.domain.domain.model.leave

data class LeaveSaveRequest(
    val created_by: String,
    val created_on: String,
    val date: String,
    val day_type: String,
    val leave_details: String,
    val leave_status: String,
    val session_type: String,
    val staff_name: String,
    val job_done :String
)