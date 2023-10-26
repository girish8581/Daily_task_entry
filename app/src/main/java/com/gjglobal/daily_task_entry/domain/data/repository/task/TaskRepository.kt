package com.gjglobal.daily_task_entry.domain.data.repository.task

import com.gjglobal.daily_task_entry.domain.domain.model.project.ProjectResponse
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.staff.GetStaffResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.AddNewTaskRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.ApiCreateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskCountResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMappingRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMasterResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusUpdateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.edittaskentry.EditTaskEntryRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise.StaffTaskDateWiseResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.TaskSummaryCountResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.TaskDataResponse
import okhttp3.ResponseBody
import java.io.File


interface TaskRepository {
    suspend fun getTaskListing(taskListRequest: TaskListRequest): TaskListResponse

    suspend fun saveTaskStatus(taskStatusRequest: TaskStatusRequest):TaskStatusResponse

    suspend fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest, id:String):TaskStatusUpdateResponse

    suspend fun getTaskCount(project_name:String):TaskCountResponse

    suspend fun getProjects():ProjectResponse

    suspend fun getStaffs(): GetStaffResponse

    suspend fun getTasks(): TaskMasterResponse

    suspend fun getTasksProjectName(project_name: String):TaskMasterResponse

    suspend fun getTaskData(taskName: String):TaskDataResponse

    suspend fun addNewTask(addNewTaskRequest: AddNewTaskRequest):ApiCreateResponse

    suspend fun addTaskMapping(taskMappingRequest: TaskMappingRequest):ApiCreateResponse

    suspend fun getRecentUpdates(recentUpdateRequest: RecentUpdateRequest):RecentUpdateResponse

    suspend fun getTasksCount(taskCountSummaryRequest: taskCountSummaryRequest):TaskSummaryCountResponse

    suspend fun getStaffTaskDateWise(staffTaskDateWiseRequest: StaffTaskDateWiseRequest): StaffTaskDateWiseResponse

    suspend fun uploadImage(file:File,user_id:String):String

    suspend fun downloadProfilePicture(id:Int): ResponseBody

    suspend fun editTaskEntry(editTaskEntryRequest: EditTaskEntryRequest, id:String):TaskStatusUpdateResponse


}