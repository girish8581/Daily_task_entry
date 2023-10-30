package com.gjglobal.daily_task_entry.domain.data.remote

import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationResponse
import retrofit2.http.GET

interface NotificationApi {
    @GET("services/getNotifications.php")
    suspend fun getNotifications(): NotificationResponse
}