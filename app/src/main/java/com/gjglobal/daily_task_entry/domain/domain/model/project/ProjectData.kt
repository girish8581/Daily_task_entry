package com.gjglobal.daily_task_entry.domain.domain.model.project

data class ProjectData(
    val description: String,
    val estimated_release_date: String,
    val id: String,
    val number_of_task_created: String,
    val project_code: String,
    val project_name: String,
    val project_status: String,
    val project_type: String,
    val released_date: String,
    val start_date: String,
    val team_assigned: String,
    val team_count: String
){

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$project_name$project_code",
            "$project_name $project_status"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}