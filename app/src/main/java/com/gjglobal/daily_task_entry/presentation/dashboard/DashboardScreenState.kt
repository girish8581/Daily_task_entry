package com.gjglobal.daily_task_entry.presentation.dashboard

import android.net.Uri
import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization


data class DashboardScreenState(
    var authorization: Authorization? = null,
    var isLoading: Boolean = false,
    var error: String = "",
    val hideBottomMenu: Boolean = false,
    val isImageUploaded: Boolean = false,
    val imageUploadResponse :String? = ""
)
