package com.gjglobal.daily_task_entry.domain.domain.model.tasklist

data class TaskListResponse(
    val `data`: List<TaskListItem>,
    val message: String,
    val status: Int
)