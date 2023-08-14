package com.gjglobal.daily_task_entry.domain.data.repository.tasklist


import com.gjglobal.daily_task_entry.domain.data.remote.TaskListingApi
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusUpdateResponse
import javax.inject.Inject

class TaskListRepositoryImpl @Inject constructor(
    private val api: TaskListingApi
) : TaskListRepository {

    override suspend fun getTaskListing(taskListRequest: TaskListRequest): TaskListResponse {
        return api.getTaskListing(staffName = taskListRequest)
    }

    override suspend fun saveTaskStatus(taskStatusRequest: TaskStatusRequest): TaskStatusResponse {
        return api.saveTaskStatus(taskStatusRequest = taskStatusRequest)
    }

    override suspend fun updateTaskStatus(
        taskUpdateRequest: TaskUpdateRequest,
        id: String
    ): TaskStatusUpdateResponse {
       return  api.updateTaskStatus(taskUpdateRequest = taskUpdateRequest,id = id)
    }

}