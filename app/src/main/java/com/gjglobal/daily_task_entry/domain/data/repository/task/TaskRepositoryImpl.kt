package com.gjglobal.daily_task_entry.domain.data.repository.task


import com.gjglobal.daily_task_entry.domain.data.remote.TaskApi
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
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise.StaffTaskDateWiseResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.TaskSummaryCountResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.TaskDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi
) : TaskRepository {

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

    override suspend fun getTaskCount(project_name: String): TaskCountResponse {
        return api.getTaskCount(projectName = project_name)
    }

    override suspend fun getTasksCount(taskCountSummaryRequest: taskCountSummaryRequest): TaskSummaryCountResponse {
        return api.getTasksCount(taskCountSummaryRequest = taskCountSummaryRequest)
    }

    override suspend fun getStaffTaskDateWise(staffTaskDateWiseRequest: StaffTaskDateWiseRequest): StaffTaskDateWiseResponse {
        return api.getStaffTaskDateWise(staffTaskDateWiseRequest = staffTaskDateWiseRequest)
    }

    override suspend fun uploadImage(file: File,user_id:String): String {
        return api.uploadImage(image = MultipartBody.Part.createFormData(name = "image",filename = file.name, body = file.asRequestBody()), user_id = user_id)
    }

    override suspend fun downloadProfilePicture(id: Int): ResponseBody {
       return api.downloadProfilePicture(id=id)
    }


    override suspend fun getProjects(): ProjectResponse {
        return api.getProjects()
    }

    override suspend fun getStaffs(): GetStaffResponse {
        return api.getStaffs()
    }

    override suspend fun getTasks(): TaskMasterResponse {
        return api.getTasks()
    }

    override suspend fun getTasksProjectName(project_name: String): TaskMasterResponse {
        return api.getTasksProjectName(project_name)
    }

    override suspend fun getTaskData(taskName: String): TaskDataResponse {
        return api.getTaskData(taskName=taskName)
    }

    override suspend fun addNewTask(addNewTaskRequest: AddNewTaskRequest): ApiCreateResponse {
        return api.addNewTask(addNewTaskRequest = addNewTaskRequest)
    }

    override suspend fun addTaskMapping(taskMappingRequest: TaskMappingRequest): ApiCreateResponse {
        return api.addTaskMapping(taskMappingRequest=taskMappingRequest)
    }

    override suspend fun getRecentUpdates(recentUpdateRequest: RecentUpdateRequest): RecentUpdateResponse {
        return api.getRecentUpdates(recentUpdateRequest = recentUpdateRequest)
    }

}