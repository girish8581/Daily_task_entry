package com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate

data class RecentUpdateRequest(
    val limit_count: String,
    val staff_name: String,
    val task_status: String,
    val ui_type :String
)