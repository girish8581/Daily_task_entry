package com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate

data class RecentUpdateResponse(
    val `data`: List<RecentUpdateItem>,
    val message: String,
    val status: Int
)