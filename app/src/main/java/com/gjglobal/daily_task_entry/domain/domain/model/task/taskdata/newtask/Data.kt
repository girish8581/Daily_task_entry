package com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.newtask

data class NewTaskItem(
    val id: String,
    val project_name: String,
    val task_details: String,
    val task_estimate_date: String,
    val task_name: String,
    val task_start_date: String,
    val task_status: String
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$task_name$project_name",
            "$project_name $task_status"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}