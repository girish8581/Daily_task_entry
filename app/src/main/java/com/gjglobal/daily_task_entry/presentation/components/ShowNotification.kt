package com.gjglobal.daily_task_entry.presentation.components

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.utils.MainActivity

fun showNotification(context: Context, message: String) {
    val notificationBuilder = NotificationCompat.Builder(context, "task_channel")
        .setSmallIcon(R.drawable.notification) // You should have a notification icon
        .setContentTitle("taskEntryNotification")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(pendingIntent)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(1, notificationBuilder.build())
    }
}