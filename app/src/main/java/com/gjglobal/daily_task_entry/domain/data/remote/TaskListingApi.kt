package com.gjglobal.daily_task_entry.domain.data.remote


import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusUpdateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskListingApi {
    //STAFF WISE TASK LIST API
    //http://65.1.250.239/services/getStaffwiseTask.php
    //body//
    //    {
    //        "staff_name":"GIRISH KUMAR.G"
    //    }

    @POST("services/getStaffwiseTask.php")
    suspend fun getTaskListing(
        @Body staffName: TaskListRequest
    ): TaskListResponse

    //http://65.1.250.239/services/createTask.php
    //body//
//    {
//        "date": "2023-08-01",
//        "work_at": "Office",
//        "staff_name": "staff_name",
//        "project_name": "project_name",
//        "task_no": "task_no",
//        "jira_no": "jira_no",
//        "task_details": "task_details",
//        "assigned_date": "2023-08-09",
//        "task_status": "task_status",
//        "project_status": "project_status",
//        "job_done":"job_done",
//        "remarks_any": "remarks_any",
//        "leave_status":"00",
//        "leave_details": "session_type",
//        "day_type": "session_type",
//        "session_type": "session_type",
//        "start_time": "00:00:00",
//        "end_time": "00:00:00",
//        "created_on": "2023-08-09 10:00:00",
//        "created_by": "1"
//    }

    @POST("services/createTask.php")
    suspend fun saveTaskStatus(@Body taskStatusRequest: TaskStatusRequest): TaskStatusResponse


    @PUT("services/updateTaskStatus.php")
    suspend fun updateTaskStatus(@Body taskUpdateRequest: TaskUpdateRequest, @Query("id") id: String): TaskStatusUpdateResponse

}