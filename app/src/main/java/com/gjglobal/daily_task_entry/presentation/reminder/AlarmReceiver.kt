package com.gjglobal.daily_task_entry.presentation.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek != Calendar.SUNDAY) {
            val workRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java).build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
