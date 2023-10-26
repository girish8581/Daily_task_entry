package com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.newtask

data class NewTaskResponse(
    val `data`: List<NewTaskItem>,
    val message: String,
    val status: Int
)