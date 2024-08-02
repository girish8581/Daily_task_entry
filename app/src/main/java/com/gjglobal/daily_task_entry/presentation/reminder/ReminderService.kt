package com.gjglobal.daily_task_entry.presentation.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.utils.MainActivity

class ReminderService : Service() {

    override fun onCreate() {
        super.onCreate()
        showNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showNotification() {
        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"
        val notificationId = 1

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Task Entry App")
            .setContentText("REMINDER: Please update today task details in the app.Please ignore if already done")
            .setSmallIcon(R.drawable.task_app_icon_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        startForeground(notificationId, notification)
    }
}
