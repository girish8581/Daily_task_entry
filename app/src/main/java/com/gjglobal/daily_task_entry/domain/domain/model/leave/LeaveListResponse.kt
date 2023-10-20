package com.gjglobal.daily_task_entry.domain.domain.model.leave

data class LeaveListResponse(
    val `data`: List<LeaveData>,
    val message: String,
    val status: Int
)