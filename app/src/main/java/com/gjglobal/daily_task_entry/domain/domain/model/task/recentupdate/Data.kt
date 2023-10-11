package com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate

data class RecentUpdateItem(
    val id:String?=null,
    val date: String?=null,
    val job_done: String? =null,
    val project_name: String? = null,
    val staff_name: String? = null,
    val task_details: String? = null,
    val task_no: String? = null,
    val task_status:String? = null,
    val leave_details:String?=null,
    val leave_status:String?=null,
    val start_time:String? = null,
    val end_time:String? = null,
    val timeTaken:String? = null,
    val completed_level:String? = null
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$staff_name$project_name",
            "$project_name $task_no"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}