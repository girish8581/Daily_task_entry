package com.gjglobal.daily_task_entry.domain.domain.model.task.qatask

data class QaTaskRequest(
    val createdBy: String,
    val createdOn: String,
    val date: String,
    val jobDone: String,
    val projectName: String,
    val staffName: String,
    val taskDetails: String,
    val taskNo: String,
    val taskStatus: String,
    val qaTaskStatus:String,
    val jiraNo :String,
    val id :String,
    val completedLevel:String,
    val qaTaskNo:String
)
