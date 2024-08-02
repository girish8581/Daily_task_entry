package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationItem

data class NotificationScreenState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var notificationList: List<NotificationItem>? = null,
    var isNotificationList: Boolean = false,
    var isNewNotification:Boolean?=false,
    var notificationCount:Int = 0,
    var readNot:List<ReadNotificationIndexModel>?=null,
    var prevNotificationCount:Int = 0,
    var isPushNotification:Int=0,
)
