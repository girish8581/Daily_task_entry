package com.gjglobal.daily_task_entry.domain.di

import com.gjglobal.daily_task_entry.domain.data.remote.TaskListingApi
import com.gjglobal.daily_task_entry.domain.data.repository.tasklist.TaskListRepository
import com.gjglobal.daily_task_entry.domain.data.repository.tasklist.TaskListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class, ServiceComponent::class)
object DataModule{
    @Provides
    internal fun provideTaskListingApi(retrofit: Retrofit): TaskListingApi {
        return retrofit.create(TaskListingApi::class.java)
    }

    @Provides
    internal fun provideTaskListRepository(api: TaskListingApi): TaskListRepository {
        return TaskListRepositoryImpl(api)
    }

}