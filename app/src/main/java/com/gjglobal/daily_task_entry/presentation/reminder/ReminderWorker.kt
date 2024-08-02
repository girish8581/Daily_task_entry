package com.gjglobal.daily_task_entry.presentation.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.utils.MainActivity
import java.util.Calendar

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek == Calendar.SUNDAY) {
            return Result.success()
        }

        // Show the notification
        showNotification()

        return Result.success()
    }

    private fun showNotification() {
        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"
        val notificationId = 1

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Task Entry App")
            .setContentText("REMINDER: Please update today task details in the app.Please ignore if already done")
            .setSmallIcon(R.drawable.task_app_icon_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
