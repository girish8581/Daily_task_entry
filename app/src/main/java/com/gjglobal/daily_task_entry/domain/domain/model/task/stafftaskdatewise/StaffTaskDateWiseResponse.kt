package com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise

data class StaffTaskDateWiseResponse(
    val `data`: List<TaskDataDateWise>,
    val message: String,
    val status: Int
)