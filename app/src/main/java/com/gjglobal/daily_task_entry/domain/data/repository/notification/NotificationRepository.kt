package com.gjglobal.daily_task_entry.domain.data.repository.notification

import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationResponse
import javax.inject.Inject

interface NotificationRepository {
    suspend fun getNotifications(): NotificationResponse
}