package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.taskview

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
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa.RecentUpdateQaRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.components.TaskCard
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.utils.formatDate

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

                viewModel.getRecentQaUpdates(
                    RecentUpdateQaRequest(
                        staff_name = staffName,
                        limit_count = "10",
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
                var qaEnable :Boolean = false
                if(taskStatus=="In QA Testing"){
                    qaEnable = true
                }
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
                                    viewModel.getTaskList(
                                        taskListRequest = taskListRequest
                                    )

                                    viewModel.getRecentQaUpdates(
                                        RecentUpdateQaRequest(
                                            staff_name = staffName,
                                            limit_count = "10",
                                        )
                                    )
                                },qaEnable = qaEnable)
                        }
                        item {
                            //Spacer(modifier = Modifier.height(10.dp))
                            println("qaEnable$qaEnable")
                            println(viewModel.state.value.isRecentUpdatesQaList!!)
                            if(qaEnable){
                                InProgressQaList(viewModel=viewModel, navController = navController)
                            }
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InProgressQaList(viewModel: TaskListViewModel,navController: NavController) {
    val leaveList1 by viewModel.recentUpdatesQaList.collectAsState()
    val context = LocalContext.current

    if (viewModel.state.value.isRecentUpdatesQaList!!) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding()
        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Text(text = "Recent QA Updates", style = TextStyle_600_14, color = ColorPrimary)
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
                                                if (item.qa_status!! == "QA Testing Passed") {
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
                                                text = item.qa_task_no!!,
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
                                                text = formatDate(item.qa_date!!),
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
                                                text = "Task No",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.task_no!!,
                                                style = TextStyle_500_12,
                                                color = Color.Magenta
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
                                                text = "Level",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.completed_level!!+ " % done",
                                                style = TextStyle_500_12,
                                                color = if(item.qa_status=="QA Testing Progress"){
                                                    Color.Red}else{
                                                    DarkGreenColor
                                                }
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
                                                text = item.qa_jira_no!!,
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
                                                text = item.qa_remarks!!,
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
                                                text = item.qa_status,
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
