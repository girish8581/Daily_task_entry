package com.gjglobal.daily_task_entry.domain.data.repository.notification

import com.gjglobal.daily_task_entry.domain.data.remote.NotificationApi
import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationResponse
import javax.inject.Inject

class NotificationRepositoryImpl@Inject constructor(
    private val api: NotificationApi
):NotificationRepository {
    override suspend fun getNotifications(): NotificationResponse {
        return api.getNotifications()
    }
}