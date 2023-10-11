package com.gjglobal.daily_task_entry.domain.domain.use_case

import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.repository.task.TaskRepository
import com.gjglobal.daily_task_entry.domain.domain.model.project.ProjectResponse
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.staff.GetStaffResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.AddNewTaskRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.ApiCreateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskCountResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMappingRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMasterResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusUpdateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise.StaffTaskDateWiseResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.TaskSummaryCountResponse
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.TaskDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class TaskListUseCase @Inject constructor(
    private val repository: TaskRepository
){


    fun getProjects(): Flow<Resource<ProjectResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getProjects()
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getStaffs(): Flow<Resource<GetStaffResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getStaffs()
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getTasks(): Flow<Resource<TaskMasterResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTasks()
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getTaskData(taskName:String): Flow<Resource<TaskDataResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTaskData(taskName=taskName)
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }



    fun getStaffTaskDateWise(staffTaskDateWiseRequest: StaffTaskDateWiseRequest): Flow<Resource<StaffTaskDateWiseResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getStaffTaskDateWise(staffTaskDateWiseRequest= staffTaskDateWiseRequest)
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getTasksProjectName(project_name: String): Flow<Resource<TaskMasterResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTasksProjectName(project_name = project_name )
                emit(Resource.Success(apiResponse))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong."))
            }
        }

    fun getTaskCount(project_name:String): Flow<Resource<TaskCountResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTaskCount(
                    project_name = project_name
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

    fun getTaskList(taskListRequest : TaskListRequest): Flow<Resource<TaskListResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTaskListing(
                    taskListRequest = taskListRequest
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

    fun getTasksCount(taskCountSummaryRequest: taskCountSummaryRequest): Flow<Resource<TaskSummaryCountResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getTasksCount(
                   taskCountSummaryRequest = taskCountSummaryRequest
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

    fun saveTaskStatus(taskStatusRequest: TaskStatusRequest ): Flow<Resource<TaskStatusResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.saveTaskStatus(
                    taskStatusRequest = taskStatusRequest
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

    fun addNewTask(addNewTaskRequest: AddNewTaskRequest ): Flow<Resource<ApiCreateResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.addNewTask(
                    addNewTaskRequest = addNewTaskRequest
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

    fun addTaskMap(taskMappingRequest: TaskMappingRequest ): Flow<Resource<ApiCreateResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.addTaskMapping(
                    taskMappingRequest = taskMappingRequest
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

    fun updateTaskStatus(taskUpdateRequest: TaskUpdateRequest,id :String): Flow<Resource<TaskStatusUpdateResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.updateTaskStatus(taskUpdateRequest = taskUpdateRequest,id = id
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

    fun getRecentUpdates(recentUpdateRequest: RecentUpdateRequest): Flow<Resource<RecentUpdateResponse>?> =
        flow {
            try {
                emit(Resource.Loading())
                val apiResponse = repository.getRecentUpdates(
                    recentUpdateRequest = recentUpdateRequest
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


    fun uploadProfilePicture(file: File,user_id:String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val apiResponse = repository.uploadImage(file, user_id)
                emit(Resource.Success(apiResponse))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong."))
        }
    }

    fun downloadProfilePicture(id:Int): Flow<Resource<ResponseBody>> = flow {
        try {
            emit(Resource.Loading())
            val apiResponse = repository.downloadProfilePicture(id = id)
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

