package com.gjglobal.daily_task_entry.presentation.dashboard.home.leave

import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveData


data class LeaveState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var isLeaveList:Boolean? = false,
    var leaveList: List<LeaveData>? = null,
    var isLeaveSaved:Boolean?=false
    )