package com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise

data class TaskDataDateWise(
    val assigned_date: String,
    val date: String,
    val end_time: String?=null,
    val id: String,
    val jira_no: String,
    val job_done: String,
    val project_name: String,
    val staff_name: String,
    val start_time: String? = null,
    val taskTime: String,
    val task_details: String,
    val task_id: String,
    val task_no: String,
    val task_status: String,
    val timeTaken: String,
    val updated_date: String,
    val SOURCE:String?=null,
    val completed_level:String?=null
)

//{"SOURCE":"b","id":"755","date":"2024-01-12","staff_name":"Anjana.P.K",
//    "assigned_date":"2024-01-12","updated_date":"2024-01-12",
//    "task_no":"PKZY-DEVP-37","task_status":"QA
//    Testing Failed","taskTime":"3","timeTaken":"00:03",
//    "task_details":"Summary report
//    new api integration","job_done":"Vip count displaying incorrectly, Api
//    issues","task_id":"755","start_time":null,
//    "end_time":null,"jira_no":"PM-51",
//    "project_name":"parkEzy","completed_level":"90"}