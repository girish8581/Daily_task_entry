package com.gjglobal.daily_task_entry.domain.domain.model.tasklist

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
    val project_status:String

){

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$staff_name$task_name",
            "$project_name $task_status"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}