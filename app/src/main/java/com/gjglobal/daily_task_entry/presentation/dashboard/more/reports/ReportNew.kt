package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports

import ShimmerEffectListView
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.FormatTime
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.EmployeeWiseReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.SummaryReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.time.DayOfWeek
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: ReportViewModel = hiltViewModel()

) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val taskViewModel : TaskAssignViewModel = hiltViewModel()
    val taskState = taskViewModel.state.value
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val state = viewModel.state.value
    var showEmpReport by remember { mutableStateOf(false) }
    var showSummaryReport by remember { mutableStateOf(false) }
    val userRole = userData?.userType

    val leaveList1 by viewModel.recentUpdatesList.collectAsState()


    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                dashViewModel.hideBottomMenu(false)
                taskViewModel.getStaffs()
                viewModel.getReports(
                    RecentUpdateRequest(
                        staff_name = staffName!!,
                        task_status = "ALL",
                        limit_count = "35",
                        ui_type = "REPORT"
                    )
                )
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }

    Column(modifier = Modifier.background(color = Color.White)) {

        ToolBar(nameOfScreen = "Reports", iconOfScreen = 0, onClick = {
            navController.popBackStack()
        }, onIconClick = {})

        //WeekStartEndDates()

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_5)))

        // Use a Box to add the plus icon at the bottom right
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(viewModel.state.value.isLoading.not()) {
                // Use LazyColumn to display the list of cards
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(leaveList1) { item ->
                        ReportCard(item = item)
                    }
                }
            }else{
                ShimmerEffectListView(isLoading = viewModel.state.value.isLoading)
            }

            ExpandableFAB(weeklyReport = {showEmpReport =true}, qaReport = {}, summary = {showSummaryReport=true})
        }
    }

    if(showEmpReport){
        EmployeeWiseReport {
            showEmpReport= false
        }
    }

    if(showSummaryReport){
        SummaryReport(viewModel=taskViewModel, summaryEnable = true, onClick = {
            showSummaryReport = false
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekStartEndDates() {
    val currentDate = LocalDate.now()

    // Calculate the start of the week (Sunday)
    val weekStartDate = currentDate.with(DayOfWeek.SUNDAY)

    // Calculate the end of the week (Saturday)
    val weekEndDate = weekStartDate.plusDays(6)

    Text("Current Date: $currentDate")
    Text("Start of the Week (Sunday): $weekStartDate")
    Text("End of the Week (Saturday): $weekEndDate")
}


@Composable
fun ExpandableFAB(weeklyReport:()-> Unit,qaReport:()-> Unit,summary:()-> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp, end = 20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                ReportItem("Task Report") {weeklyReport.invoke()
                    isExpanded = false}
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("QA Report") {qaReport.invoke()
                    isExpanded = false}
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("Summary") {summary.invoke()
                    isExpanded = false}

            }
        }

        FloatingActionButton(
            onClick = { isExpanded = !isExpanded }
        ) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Expandable FAB"
            )
        }
    }
}
@Composable
fun ReportItem(reportName:String,onClick:()-> Unit){
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(16.dp)) // Adjust the corner radius as needed
            .background(ColorPrimary)
            .clickable {
                    onClick.invoke()
            }
    ) {
        Text(
            text = reportName,
            style = TextStyle_600_12,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp)
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportCard(item:RecentUpdateItem){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_10),
                    horizontal = dimensionResource(id = R.dimen.dimen_10)
                ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
            elevation = 10.dp
        ) {


            Column(
//                                        modifier = Modifier
//                                        .background(color = Color.White)
                modifier = Modifier
                    .background(
                        if (item.leave_status == "0") {
                            Color.White
                        } else {
                            todoColor
                        }
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (item.leave_status == "0") {
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
                            style = TextStyle_600_14,
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
                        Text(
                            text = startTime + " to  " + endTime +""+ if(item.timeTaken.isNullOrEmpty().not()){" / "+item.timeTaken!!.toDouble()/60+" Hrs"}else{""},
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
                            text = "Job Done",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = item.job_done!!,
                            style = TextStyle_500_12,
                            color = DarkGreen
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
                            text = item.task_status!!,
                            style = TextStyle_500_12,
                            color = if (item.task_status =="COMPLETED"){
                                ColorPrimary
                            }else{Color.Red}
                        )
                    }
                } else{

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
                            style = TextStyle_600_14,
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
                            text = "Leave",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = item.leave_details!!,
                            style = TextStyle_500_12,
                            color = ColorPrimary
                        )
                    }

                }
            }
        }
    }
}


