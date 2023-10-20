package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports

import com.gjglobal.daily_task_entry.domain.domain.model.staff.StaffData
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem

data class ReportState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var reportList:List<RecentUpdateItem>? = null,
    var isReportList:Boolean? = false,
    var staffList: List<StaffData>? = null,
    var showEmployeeReport:Boolean?= false
)