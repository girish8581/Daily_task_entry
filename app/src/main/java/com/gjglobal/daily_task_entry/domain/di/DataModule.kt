package com.gjglobal.daily_task_entry.domain.di

import com.gjglobal.daily_task_entry.domain.data.remote.LeaveSaveApi
import com.gjglobal.daily_task_entry.domain.data.remote.LoginApi
import com.gjglobal.daily_task_entry.domain.data.remote.TaskApi
import com.gjglobal.daily_task_entry.domain.data.repository.leave.LeaveRepository
import com.gjglobal.daily_task_entry.domain.data.repository.leave.LeaveRepositoryImpl
import com.gjglobal.daily_task_entry.domain.data.repository.login.LoginRepository
import com.gjglobal.daily_task_entry.domain.data.repository.login.LoginRepositoryImpl
import com.gjglobal.daily_task_entry.domain.data.repository.task.TaskRepository
import com.gjglobal.daily_task_entry.domain.data.repository.task.TaskRepositoryImpl
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
    internal fun provideTaskListingApi(retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @Provides
    internal fun provideTaskListRepository(api: TaskApi): TaskRepository {
        return TaskRepositoryImpl(api)
    }

    @Provides
    internal fun provideLeaveSaveApi(retrofit: Retrofit): LeaveSaveApi {
        return retrofit.create(LeaveSaveApi::class.java)
    }

    @Provides
    internal fun provideLeaveRepository(api: LeaveSaveApi): LeaveRepository {
        return LeaveRepositoryImpl(api)
    }

    @Provides
    internal fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    internal fun provideLoginRepository(api: LoginApi): LoginRepository {
        return LoginRepositoryImpl(api)
    }

}