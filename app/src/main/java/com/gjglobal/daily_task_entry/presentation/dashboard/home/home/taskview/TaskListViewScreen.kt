package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.taskview

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.components.TaskCard
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskListViewScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskListViewModel = hiltViewModel(),
    taskStatus:String
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    //val taskStatus :String = "COMPLETED"

    var taskListRequest : TaskListRequest?= null
    taskListRequest = TaskListRequest(staff_name = staffName!!, task_status = taskStatus)
    var showSuccess by remember { mutableStateOf(false) }

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                viewModel.getTaskList(
                    taskListRequest = taskListRequest
                )
                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            ToolBar(nameOfScreen = "$taskStatus Tasks",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))

            if (viewModel.state.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
             
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }else{
                val taskList1 by viewModel.taskList.collectAsState()
                if (taskList1.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(taskList1) { item ->
                            TaskCard( modifier = Modifier,
                                onClick = {
                                    showSuccess = true
                                },list = item,
                                viewModel,buttonEnable = false,
                                taskStatus = taskStatus
                            , onStatusUpdate = {
                                    showSuccess = true
                                })
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }else{
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable {}, contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_records_found),
                                contentDescription = stringResource(id = R.string.no_records_found_txt)
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_12)))
                            Text(
                                text = stringResource(id = R.string.no_records_found_txt),
                                style = TextStyle_400_14
                            )
                        }
                    }
                }
            }
        }
    }

    if(showSuccess){
        Messagebox(onSuccess = {
            showSuccess = false
            viewModel.getTaskList(
                taskListRequest = taskListRequest
            )
           // navController.navigate(Screen.TaskListToDoViewScreen.route)

        }, message = "Status updated!!" )
    }
}
