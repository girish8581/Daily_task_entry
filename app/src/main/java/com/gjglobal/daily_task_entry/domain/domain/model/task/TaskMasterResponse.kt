package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskMasterResponse(
    val `data`: List<TaskMasterData>,
    val message: String,
    val status: Int
)