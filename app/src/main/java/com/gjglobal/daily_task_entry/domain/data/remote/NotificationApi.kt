package com.gjglobal.daily_task_entry.domain.data.remote

import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("services/getNotifications.php")
    suspend fun getNotifications(@Query("userName") userName: String? = null): NotificationResponse

    @GET("services/getNotifications.php")
    suspend fun getNotificationsAdmin(): NotificationResponse
}