package com.gjglobal.daily_task_entry.presentation.dashboard

import com.gjglobal.hms_gj.domain.domain.model.login.Authorization


data class DashboardScreenState(
    var authorization: Authorization? = null,
    var isLoading: Boolean = false,
    var error: String = "",
    val hideBottomMenu: Boolean = false
)
