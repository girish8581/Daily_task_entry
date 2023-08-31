package com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata

data class TaskDataResponse(
    val `data`: TaskDetails,
    val message: String,
    val status: Int
)