package com.gjglobal.daily_task_entry.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ActivityRetainedComponent::class, ServiceComponent::class)
object DataModule {



}