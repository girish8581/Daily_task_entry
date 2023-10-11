package com.gjglobal.daily_task_entry.presentation.dashboard

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.use_case.TaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val taskListUseCase: TaskListUseCase,
    cacheManager: CacheManager,
    application: Application
) : ViewModel() {

    //var selectedImageUri = mutableStateOf<Uri?>(null)
    companion object {
        var profilePic: File? = null
    }

    private val _state = mutableStateOf(DashboardScreenState())
    val state: State<DashboardScreenState> = _state
    val contentResolver: ContentResolver = application.contentResolver


    init {
        _state.value = _state.value.copy(authorization = cacheManager.getAuthResponse())
    }

    fun hideBottomMenu(boolean: Boolean) {
        _state.value = _state.value.copy(hideBottomMenu = boolean)
    }



//    fun uploadImageToApi(imageBytes: ByteArray) {
//        taskListUseCase.getTasksProjectName(project_name=project_name)
//
//            .onEach { result ->
//                when (result) {
//
//                    is Resource.Success -> {
//                        Log.e("tasks",result.data.toString())
//                        if (result.data.toString().isNotEmpty()) {
//                            _state.value = _state.value.copy(isTaskList = true)
//                            _state.value =
//                                _state.value.copy(isTaskLoading = false, isTaskList = true, taskList = result.data?.data)
//
//                            Log.e("result", result.data.toString())
//                        } else {
//                            _state.value = _state.value.copy(
//                                isTaskLoading = false,isTaskList = false
//                            )
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        _state.value = _state.value.copy(isStaffList = false)
//                        _state.value = _state.value.copy(
//                            isTaskLoading = false,
//                            error = result.message ?: "An unexpected error occurred"
//
//                        )
//                    }
//
//                    is Resource.Loading -> {
//                        _state.value = _state.value.copy(isTaskLoading = true)
//                    }
//
//                    else -> {}
//                }
//            }.launchIn(viewModelScope)
//    }

    fun uploadProfilePicture(activity: Activity,uri:Uri,user_id:String) {
        val file = getFile(activity, uri)

        println(file)

        taskListUseCase.uploadProfilePicture(
            file = file, user_id = user_id
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isImageUploaded = true,imageUploadResponse = result.data)
                    profilePic = file

                    println(_state.value.imageUploadResponse)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isImageUploaded = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    @Throws(IOException::class)
    private fun getFile(context: Context, uri: Uri): File {
        val destinationFilename = File((context.filesDir.path + File.separatorChar) + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(ins, destinationFilename)
                }
            }
        } catch (ex: Exception) {
            ex.message?.let { Log.e("Save File", it) }
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            ex.message?.let { Log.e("Save File", it) }
            ex.printStackTrace()
        }
    }


    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }





}