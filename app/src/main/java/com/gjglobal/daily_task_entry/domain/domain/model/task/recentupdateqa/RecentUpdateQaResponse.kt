package com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa

data class RecentUpdateQaResponse(
    val `data`: List<RecentUpdateQaItem>,
    val message: String,
    val status: Int
)