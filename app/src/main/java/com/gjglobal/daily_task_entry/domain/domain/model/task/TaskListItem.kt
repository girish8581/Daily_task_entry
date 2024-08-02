package com.gjglobal.daily_task_entry.domain.domain.model.task

data class TaskListItem(
    val assigned_date: String,
    val id: String,
    val project_id: String,
    val project_name: String,
    val staff_id: String,
    val staff_name: String,
    val task_id: String,
    val task_name: String,
    val task_status: String,
    val task_details:String,
    val task_jira_no:String,
    val project_status:String,
    val updated_date:String,
    val entry_date:String,
    val taskTime:String?=null,
    val time_taken:String?=null,
    val completed_level:String,
    val qa_task_no:String,
    val qa_task_status:String,
    val qa_created_on :String,
    val qa_staff_name:String? = null,
    val qa_completed_level:String? = null,
    val hours:String? = null,
    val qa_last_update:String? = null
){

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$task_jira_no$task_name",
            "$project_name $task_status $task_details"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}