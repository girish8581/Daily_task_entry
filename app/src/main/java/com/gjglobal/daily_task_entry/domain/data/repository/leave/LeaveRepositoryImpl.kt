package com.gjglobal.daily_task_entry.domain.data.repository.leave

import com.gjglobal.daily_task_entry.domain.data.remote.LeaveSaveApi
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveResponse
import javax.inject.Inject

class LeaveRepositoryImpl @Inject constructor(
    private val api: LeaveSaveApi
):LeaveRepository {
    override suspend fun saveLeave(leaveSaveRequest: LeaveSaveRequest): LeaveSaveResponse {
        return api.saveLeave(leaveSaveRequest = leaveSaveRequest)
    }

    override suspend fun getStaffLeaveList(leaveListRequest: LeaveListRequest): LeaveListResponse {
        return api.getStaffLeaveList(leaveListRequest=leaveListRequest)
    }

}