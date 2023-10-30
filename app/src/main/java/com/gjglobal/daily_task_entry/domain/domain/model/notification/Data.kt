package com.gjglobal.daily_task_entry.domain.domain.model.notification

data class NotificationItem(
    val created_on: String,
    val delete_status: String,
    val id: String,
    val message: String,
    val read_status: String,
    val userName: String,
    val userType: String
)