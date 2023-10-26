package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Color
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
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.components.TaskCard
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.component.EditTask
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskListScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    var taskListRequest : TaskListRequest?= null
    taskListRequest = TaskListRequest(staff_name = staffName!!, task_status = "IN PROGRESS")
    var showSuccess by remember { mutableStateOf(false) }
    var listItem by remember { mutableStateOf<RecentUpdateItem?>(null) }




    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                viewModel.getTaskList(
                    taskListRequest = taskListRequest
                )

                viewModel.getRecentUpdates(
                    RecentUpdateRequest(
                        staff_name = staffName,
                        task_status = "IN PROGRESS",
                        limit_count = "5",
                        ui_type = "UI"
                    )
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
            ToolBar(nameOfScreen = "In progress Tasks",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))

                val taskList1 by viewModel.taskList.collectAsState()

                if (taskList1.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(taskList1) { item ->
                            TaskCard( modifier = Modifier,
                                onClick = {
                                    showSuccess = true
                                    viewModel.getTaskList(
                                        taskListRequest = taskListRequest
                                    )
                                    viewModel.getRecentUpdates(
                                        RecentUpdateRequest(
                                            staff_name = staffName,
                                            task_status = "IN PROGRESS",
                                            limit_count = "5",
                                            ui_type = "UI"
                                        )
                                    )
                                },list = item,
                                viewModel,buttonEnable = true,
                                taskStatus = "IN PROGRESS" , onStatusUpdate = {},
                                qaEnable = false)
                        }
                        item {
                            InProgressList(viewModel = viewModel, navController = navController)
                        }
                    }
                }else{
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                        if(viewModel.state.value.isLoading.not()){
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .clickable {}, contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    InProgressList(viewModel = viewModel,navController=navController)

                                    Image(
                                        painter = painterResource(id = R.drawable.no_records_found),
                                        contentDescription = stringResource(id = R.string.no_records_found_txt)
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_12)))
                                    Text(
                                        text = "No pending tasks found",
                                        style = TextStyle_400_14
                                    )
                                }
                            }
                        }else {
                            CircularProgressIndicator(
                                color = ColorPrimary,
                                modifier = Modifier
                                    .padding(top = 200.dp)
                                    .size(30.dp)
                            )
                        }
                    }
                }


        }
    }

    if(showSuccess){
        Messagebox(onSuccess = {
            viewModel.getTaskList(
                taskListRequest = taskListRequest
            )
            showSuccess = false
            //onCancelClick.invoke()
        }, message = "Status updated!!" )
    }

    if(viewModel.state.value.editData!!){
        EditTask(item = viewModel.state.value.recentUpdateItem!!,
            onClickCancelBtn = {viewModel.isEditTask(false)}, onEditSuccess = {
                viewModel.showEditSuccess(true)
            }, viewModel = viewModel
        )
    }

    if(viewModel.state.value.editDataSuccess!!){
        Messagebox(onSuccess = { viewModel.showEditSuccess(false)
            navController.navigate(Screen.TaskListScreen.route)},
            message = "Task edited successfully!")

    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InProgressList(viewModel: TaskListViewModel,navController: NavController) {
    val leaveList1 by viewModel.recentUpdatesList.collectAsState()
    val context = LocalContext.current

    if (viewModel.state.value.isRecentUpdatesList!!) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding()
        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Text(text = "Recent Updates", style = TextStyle_600_14, color = ColorPrimary)
                }
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn(modifier = Modifier.height(600.dp)) {
                    items(leaveList1) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    //.height(130.dp)
                                    .clickable {
                                        viewModel.editTaskItem(item)
                                        viewModel.isEditTask(true)

                                    }
                                    .padding(
                                        vertical = dimensionResource(id = R.dimen.dimen_10),
                                        horizontal = dimensionResource(id = R.dimen.dimen_10)
                                    ),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
                                elevation = 10.dp
                            ) {

                                if (item.project_name.isNullOrBlank().not()) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                if (item.task_status!! == "COMPLETED") {
                                                    doneColor
                                                } else {
                                                    inProgressColor
                                                }
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 10.dp
                                                )
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = item.project_name!!,
                                                style = TextStyle_600_12,
                                                color = Color.Red
                                            )

                                            Text(
                                                text = item.task_no!!,
                                                style = TextStyle_600_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Date",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = formatDate(item.date!!),
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Task",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.task_details!!,
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Time Taken",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )
                                            //Spacer(modifier = Modifier.width(30.dp))
                                            val startTime = FormatTime(item.start_time!!)
                                            val endTime = FormatTime(item.end_time!!)
//                                            Text(
//                                                text = startTime + " to  " + endTime +""+ if(item.timeTaken.isNullOrEmpty().not()){" / "+item.timeTaken!!.toDouble()/60+" Hrs"}else{""},
//                                                style = TextStyle_500_12,
//                                                color = ColorPrimary
//                                            )

                                            Text(
                                                text = "$startTime to $endTime" +
                                                        (if (!item.timeTaken.isNullOrEmpty()) {
                                                            " / " + String.format("%.2f", item.timeTaken.toDouble() / 60) + " Hrs"
                                                        } else {
                                                            ""
                                                        }),
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Level",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.completed_level!!+ " % done",
                                                style = TextStyle_500_12,
                                                color = if(item.task_status=="IN PROGRESS"){
                                                    Color.Red}else{
                                                    DarkGreenColor}
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Break Hours",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = (item.total_break_hours!!.toDouble()/60).toString()+ "Hrs",
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Jira Id",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.jira_no!!,
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Job Done",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.job_done!!,
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Status",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.task_status,
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

            }
        }

    } else {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (viewModel.state.value.isLoading) {
                Text(
                    text = "Loading...",
                    style = TextStyle_400_14,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
        }

    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormatTime(time :String):String{
    val formattedTime = remember { mutableStateOf("") }
    try {
        val parsedTime = LocalTime.parse(time)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        formattedTime.value = parsedTime.format(formatter)

    } catch (e: Exception) {
        // Handle invalid time format
        formattedTime.value = "Invalid time"
    }
    return formattedTime.value
}


