package com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount

data class TaskSummaryCountResponse(
    val `data`: List<TaskSummaryCountData>,
    val message: String,
    val status: Int
)