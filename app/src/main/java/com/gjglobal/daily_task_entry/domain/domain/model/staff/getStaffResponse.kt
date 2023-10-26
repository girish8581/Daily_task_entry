package com.gjglobal.daily_task_entry.domain.domain.model.staff

data class GetStaffResponse(
    val `data`: List<StaffData>,
    val message: String,
    val status: Int
)