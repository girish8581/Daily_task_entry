package com.gjglobal.daily_task_entry.domain.data.repository.leave

import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveResponse

interface LeaveRepository {
    suspend fun saveLeave(leaveSaveRequest: LeaveSaveRequest): LeaveSaveResponse

    suspend fun getStaffLeaveList(leaveListRequest: LeaveListRequest):LeaveListResponse
}