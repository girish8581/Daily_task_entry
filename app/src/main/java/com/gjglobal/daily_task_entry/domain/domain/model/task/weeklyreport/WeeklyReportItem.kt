package com.gjglobal.daily_task_entry.domain.domain.model.task.weeklyreport

data class WeeklyReportItem(
    val QA_Acceptance: String?=null,
    val TL_Acceptance: String?=null,
    val TaskNo: String? = null,
    val latestDate: String,
    val resourceName: String,
    val sum_timeTaken: String,
    val taskId: String? = null,
    val taskName: String,
    val task_no: String,
    val task_no_count: String,
    val latest_completed_level:String
)