package com.gjglobal.daily_task_entry.presentation.dashboard.home.home

import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.TaskSummaryCountData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

data class HomeScreenState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var taskCount: List<TaskSummaryCountData>? = null,
    var isTaskCount:Boolean?=false,
    var isLoadProfilePicture:Boolean?=false,
    var profilePic: File?=null
)