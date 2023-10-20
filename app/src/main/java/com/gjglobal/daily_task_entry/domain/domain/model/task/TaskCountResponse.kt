package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskCountResponse(
    val `data`: TaskCountData,
    val message: String,
    val status: Int
)