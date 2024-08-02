package com.gjglobal.daily_task_entry.domain.domain.model.otherjob

import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem

data class AddNewOtherJobRequest (
    val job_date: String,
    val project_name: String,
    val job_type: String,
    val job_hours: String,
    val job_details: String,
    val staff_name: String,
)


data class OtherJobResponse (
    val `data`: List<OtherJobData>,
    val message: String,
    val status: Int
)


data class StaffName (
    val staffName: String
)

data class OtherJobData (
    val id: String? = null,
    val actDate: String? = null,
    val actType: String? = null,
    val activity: String? = null,
    val projectName: String? = null,
    val actTime: String? = null,
    val staffName: String? = null,
    val createdDatetime: String? = null
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$staffName$actDate",
            "$actType $projectName"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
