package com.gjglobal.daily_task_entry.domain.domain.use_case

import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.repository.notification.NotificationRepository
import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
)  {

    fun getNotification(): Flow<Resource<NotificationResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getNotifications()
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }
}