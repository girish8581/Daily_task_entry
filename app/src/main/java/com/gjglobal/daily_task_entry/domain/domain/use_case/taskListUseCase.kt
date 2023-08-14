package com.gjglobal.daily_task_entry.domain.domain.use_case

import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.repository.tasklist.TaskListRepository
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusUpdateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TaskListUseCase @Inject constructor(
    private val repository: TaskListRepository
){
    fun getTaskList(taskListRequest : TaskListRequest): Flow<Resource<TaskListResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTaskListing(
                    taskListRequest = taskListRequest
                )
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun saveTaskStatus(taskStatusRequest: TaskStatusRequest ): Flow<Resource<TaskStatusResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.saveTaskStatus(
                    taskStatusRequest = taskStatusRequest
                )
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest,id :String): Flow<Resource<TaskStatusUpdateResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.updateTaskStatus(taskUpdateRequest = taskUpdateRequest,id = id
                )
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

