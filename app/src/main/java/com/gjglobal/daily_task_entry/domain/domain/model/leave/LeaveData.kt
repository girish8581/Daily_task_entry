package com.gjglobal.daily_task_entry.domain.domain.model.leave

data class LeaveData(
    val created_on: String,
    val date: String,
    val day_type: String,
    val leave_details: String,
    val leave_status: String,
    val session_type: String,
    val staff_name: String
){

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$staff_name$date",
            "$leave_details $day_type"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}