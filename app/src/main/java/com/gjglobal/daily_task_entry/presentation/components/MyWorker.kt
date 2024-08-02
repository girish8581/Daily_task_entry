package com.gjglobal.daily_task_entry.presentation.components

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // This is where you put the code for your task to run every 15 minutes.
        // Replace this with your actual task logic.
        //Timber.d("MyWorker is running...")
       // println("MyWorker is running...")

        return Result.success()
    }
}
