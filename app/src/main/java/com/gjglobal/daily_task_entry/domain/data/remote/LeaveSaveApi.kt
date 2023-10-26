package com.gjglobal.daily_task_entry.domain.data.remote

import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LeaveSaveApi {

    @POST("services/createLeave.php")
    suspend fun saveLeave(@Body leaveSaveRequest: LeaveSaveRequest): LeaveSaveResponse

    @POST("services/getLeave.php")
    suspend fun getStaffLeaveList(@Body leaveListRequest: LeaveListRequest): LeaveListResponse

}