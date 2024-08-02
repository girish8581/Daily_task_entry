package com.gjglobal.daily_task_entry.presentation.dashboard.home.otherjobs

import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.OtherJobData

data class OtherJobState (
    var isLoading: Boolean = false,
    var error: String? = null,
    var isOtherJobList:Boolean? = false,
    var otherJobList: List<OtherJobData>? = null,
    var isotherJobSaved:Boolean?=false
)