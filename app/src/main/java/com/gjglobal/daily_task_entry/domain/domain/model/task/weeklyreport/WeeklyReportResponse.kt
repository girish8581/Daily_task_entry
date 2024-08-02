package com.gjglobal.daily_task_entry.domain.domain.model.task.weeklyreport

data class WeeklyReportResponse(
    val `data`: List<WeeklyReportItem>,
    val message: String,
    val status: Int
)