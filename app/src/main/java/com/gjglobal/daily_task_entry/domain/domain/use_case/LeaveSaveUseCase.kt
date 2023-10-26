package com.gjglobal.daily_task_entry.domain.domain.use_case

import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.repository.leave.LeaveRepository
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LeaveSaveUseCase @Inject constructor(
    private val repository: LeaveRepository
) {
    fun saveLeave(leaveSaveRequest: LeaveSaveRequest): Flow<Resource<LeaveSaveResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.saveLeave(
                    leaveSaveRequest = leaveSaveRequest
                )
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getLeaveList(leaveListRequest: LeaveListRequest): Flow<Resource<LeaveListResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getStaffLeaveList(
                    leaveListRequest = leaveListRequest
                )
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }
}