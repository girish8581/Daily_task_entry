package com.gjglobal.daily_task_entry.domain.domain.model.notification

data class NotificationResponse(
    val `data`: List<NotificationItem>,
    val message: String,
    val status: Int
)