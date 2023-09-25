package com.gjglobal.daily_task_entry.domain.domain.model.requestmodel

data class StaffTaskDateWiseRequest(
    val from_date: String,
    val staff_name: String,
    val to_date: String
)