package com.gjglobal.daily_task_entry.domain.data.remote


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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    //STAFF WISE TASK LIST API
    //http://65.1.250.239/services/getStaffwiseTask.php
    @POST("services/getStaffwiseTask.php")
    suspend fun getTaskListing(
        @Body staffName: TaskListRequest
    ): TaskListResponse

    //http://65.1.250.239/services/createTask.php

    @POST("services/createDailyTask.php")
    suspend fun saveTaskStatus(@Body taskStatusRequest: TaskStatusRequest): TaskStatusResponse

    @POST("services/createTaskMapping.php")
    suspend fun addTaskMapping(@Body taskMappingRequest: TaskMappingRequest): ApiCreateResponse

    @POST("services/addNewTask.php")
    suspend fun addNewTask(@Body addNewTaskRequest: AddNewTaskRequest): ApiCreateResponse


    @GET("services/getProjects.php")
        suspend fun getTaskCount(@Query("project_name") projectName: String): TaskCountResponse

    @GET("services/getProjects.php")
    suspend fun getProjects(): ProjectResponse

    @GET("services/getStaffMaster.php")
    suspend fun getStaffs(): GetStaffResponse


    @GET("services/getTaskMaster.php")
    suspend fun getTasks(): TaskMasterResponse


    @GET("services/getTaskMaster.php")
    suspend fun getTasksProjectName(@Query("project_name") projectName: String): TaskMasterResponse


    @GET("services/getTaskData.php")
    suspend fun getTaskData(@Query("task_name") taskName: String): TaskDataResponse

    @PUT("services/updateTaskStatus.php")
    suspend fun updateTaskStatus(@Body taskUpdateRequest: TaskUpdateRequest, @Query("id") id: String): TaskStatusUpdateResponse

    @POST("services/getRecentUpdates.php")
    suspend fun getRecentUpdates(@Body recentUpdateRequest: RecentUpdateRequest): RecentUpdateResponse

    @POST("services/getTaskCount.php")
    suspend fun getTasksCount(@Body taskCountSummaryRequest: taskCountSummaryRequest): TaskSummaryCountResponse

    ///localhost/taskApi/services/getStaffTaskDatewise.php
    @POST("services/getStaffTaskDateWise.php")
    suspend fun getStaffTaskDateWise(
        @Body staffTaskDateWiseRequest: StaffTaskDateWiseRequest
    ): StaffTaskDateWiseResponse

    @Multipart
    @POST("services/image_upload_api.php")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("user_id") user_id:String
    ): String

    //"/services/image_api.php?id=$id"
    @GET("services/image_api.php")
    suspend fun downloadProfilePicture(@Query("id") id: Int): ResponseBody

    @PUT("services/editTaskEntry.php")
    suspend fun editDailyTaskEntry(@Body editTaskEntryRequest: EditTaskEntryRequest, @Query("id") id: String): TaskStatusUpdateResponse


}