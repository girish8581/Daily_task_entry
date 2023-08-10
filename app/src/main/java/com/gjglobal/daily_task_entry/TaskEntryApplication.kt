package com.gjglobal.daily_task_entry

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskEntryApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TaskEntryApplication
            private set
    }
}