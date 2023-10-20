package com.gjglobal.daily_task_entry.domain.domain.model.project

data class ProjectResponse(
    val `data`: List<ProjectData>,
    val message: String,
    val status: Int
)