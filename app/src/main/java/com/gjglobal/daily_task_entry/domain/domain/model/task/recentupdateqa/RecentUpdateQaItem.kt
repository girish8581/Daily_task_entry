package com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa



data class RecentUpdateQaItem(
    val id:String?=null,
    val qa_date: String?=null,
    val project_name: String? = null,
    val task_no: String? = null,
    val qa_task_no: String? = null,
    val qa_jira_no:String? = null,
    val task_details: String? = null,
    val qa_remarks: String? =null,
    val qa_status:String? = null,
    val updated_by:String? = null,
    val created_on:String? = null,
    val completed_level:String? = null,
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$updated_by$project_name",
            "$project_name $qa_task_no"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}