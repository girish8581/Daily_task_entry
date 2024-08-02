package com.gjglobal.daily_task_entry.presentation.reminder

import android.content.Context
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import java.util.Calendar

fun scheduleReminder(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    val now = Calendar.getInstance()
    val scheduledTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 18)
        set(Calendar.MINUTE, 15)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(now)) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val delay = scheduledTime.timeInMillis - now.timeInMillis

    val dailyWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .addTag("daily_reminder")
        .build()

    WorkManager.getInstance(context).enqueue(dailyWorkRequest)
}

