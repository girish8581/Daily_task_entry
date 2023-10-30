package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationItem

data class NotificationScreenState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var notificationList: List<NotificationItem>? = null,
    var isNotificationList: Boolean? = false,
)
