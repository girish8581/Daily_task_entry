package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjglobal.daily_task_entry.core.Resource
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationItem
import com.gjglobal.daily_task_entry.domain.domain.use_case.NotificationUseCase
import com.gjglobal.daily_task_entry.presentation.components.showNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel  @Inject constructor(
    private val useCase: NotificationUseCase,
    cacheManager: CacheManager
):ViewModel() {

    private val _state = mutableStateOf(NotificationScreenState())
    val state: State<NotificationScreenState> = _state
    var i :Int = 0
    fun getNotification(userName:String) {
        useCase.getNotification(userName = userName)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value =
                                _state.value.copy(isLoading = false, isNotificationList = true, notificationList = result.data?.data)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isNotificationList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isNotificationList = false)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun getNotificationAdmin() {
        useCase.getNotificationAdmin()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.toString().isNotEmpty()) {
                            _state.value =
                                _state.value.copy(isLoading = false, isNotificationList = true, notificationList = result.data?.data)
                            Log.e("result", result.data.toString())
                        } else {
                            _state.value = _state.value.copy(
                                isNotificationList = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isNotificationList = false)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"

                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }


    private fun updateNotification(isNew:Boolean){
        i += 1
        println(i)
        _state.value = _state.value.copy(isNewNotification = isNew,isPushNotification = i)
    }




    fun notificationManager(prevListCount:Int,
                            newList:List<NotificationItem>,context:Context){
        if(prevListCount<newList.size) {
            updateNotification(true)
            if(_state.value.isPushNotification==1){
                showPushNotification(context = context)
            }
        }else{
            updateNotification(false)
        }
    }


    private fun showPushNotification(context: Context){
            showNotification(context =context,"New notifications in Daily Task App")
    }



    fun updateReadStatus(){
        val count :Int =  _state.value.prevNotificationCount + 1
        _state.value = _state.value.copy(prevNotificationCount=count)

    }

    fun updatePrevCount(cacheManager: CacheManager){
        val count = cacheManager.getNotificationStatus().size
        _state.value = _state.value.copy(prevNotificationCount=count)

    }

    fun saveNotificationReadStatus(notificationIndex: Int,cacheManager: CacheManager){
        val newElement = ReadNotificationIndexModel(notificationIndex, true)
        val updatedList = state.value.readNot.orEmpty() + newElement
        state.value.readNot = updatedList
        cacheManager.appendNotificationStatus(newElement)

    }

    fun checkReadNotificationIndex(
        notificationIndex: Int,cacheManager: CacheManager
    ): Boolean {
        // Assuming state.readNot is a list of ReadNotificationIndexModel
        val list = cacheManager.getNotificationStatus()
        return list.any { it.notificationIndex == notificationIndex && it.readStatus } ?: false
    }
}

data class ReadNotificationIndexModel(
    val notificationIndex:Int,val readStatus:Boolean
)

