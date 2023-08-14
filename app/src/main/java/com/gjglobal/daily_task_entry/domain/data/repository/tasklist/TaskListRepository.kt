package com.gjglobal.daily_task_entry.domain.data.repository.tasklist

import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusUpdateResponse


interface TaskListRepository {
    suspend fun getTaskListing(taskListRequest: TaskListRequest): TaskListResponse

    suspend fun saveTaskStatus(taskStatusRequest: TaskStatusRequest):TaskStatusResponse

    suspend fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest, id:String):TaskStatusUpdateResponse

}