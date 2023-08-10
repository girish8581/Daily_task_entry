package com.gjglobal.daily_task_entry.presentation.dashboard.more

import com.gjglobal.daily_task_entry.domain.domain.model.logout.LogoutResponse


data class MoreState(
    val logoutData : LogoutResponse? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
)
