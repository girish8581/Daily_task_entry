package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports

import ShimmerEffectListView
import android.app.Activity
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
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateRequest
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToastMessage
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.FormatTime
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.EmployeeWiseReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.GjMonthlyReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.GjWeeklyReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.ReportModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.SummaryReport
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.GraphColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import com.gjglobal.daily_task_entry.presentation.utils.convertDate
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.time.DayOfWeek
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReportScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: ReportViewModel = hiltViewModel()

) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val taskViewModel: TaskAssignViewModel = hiltViewModel()
    val taskState = taskViewModel.state.value
    val state = viewModel.state.value
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    var showEmpReport by remember { mutableStateOf(false) }
    var showSummaryReport by remember { mutableStateOf(false) }
    var qaReport by remember { mutableStateOf(false) }
    var weeklyReport by remember { mutableStateOf(false) }
    var monthlyReport by remember { mutableStateOf(false) }
    val userRole = userData?.userType

    val currentDate = LocalDate.now()
    val dayOfWeek = currentDate.dayOfWeek.value // 1 for Monday, 7 for Sunday
    // Calculate the start of the week based on the day of the week
//    val weekStartDate = currentDate.minusDays((dayOfWeek - 1).toLong())
//    // Calculate the end of the week (Saturday)
//    val daysUntilEndOfWeek = DayOfWeek.SUNDAY.value - dayOfWeek
//    val weekEndDate = weekStartDate.plusDays(daysUntilEndOfWeek.toLong())

    val selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val weekStartDate = selectedDate.with(DayOfWeek.MONDAY)
    val weekEndDate = selectedDate.with(DayOfWeek.SUNDAY)
//
//    val weekStartDate = "2024-04-15"
//    val weekEndDate = "2024-04-21"

    val monthStartDate = selectedDate.minusMonths(1).withDayOfMonth(1)
    val monthEndDate = selectedDate.withDayOfMonth(1).minusDays(1)


    val leaveList1 by viewModel.recentUpdatesList.collectAsState()


    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                dashViewModel.hideBottomMenu(false)
                taskViewModel.getStaffs()

                if (userRole == "ADMIN") {
                    viewModel.getReports(
                        RecentUpdateRequest(
                            staff_name = "ALL",
                            task_status = "ALL",
                            limit_count = "35",
                            ui_type = "REPORT"
                        )
                    )
                    viewModel.getWeeklyReport(
                        StaffTaskDateWiseRequest(
                            from_date = weekStartDate.toString(),
                            staff_name = "ALL",
                            to_date = weekEndDate.toString()
                        )
                    )

                    taskViewModel.getStaffTaskDateWise(
                        StaffTaskDateWiseRequest(
                            from_date = weekStartDate.toString(),
                            staff_name = "ALL",
                            to_date = weekEndDate.toString()
                        )
                    )

                } else {

                    viewModel.getReports(
                        RecentUpdateRequest(
                            staff_name = staffName!!,
                            task_status = "ALL",
                            limit_count = "35",
                            ui_type = "REPORT"
                        )
                    )

                    viewModel.getWeeklyReport(
                        StaffTaskDateWiseRequest(
                            from_date = weekStartDate.toString(),
                            staff_name = staffName,
                            to_date = weekEndDate.toString()
                        )
                    )
                    taskViewModel.getStaffTaskDateWise(
                        StaffTaskDateWiseRequest(
                            from_date = weekStartDate.toString(),
                            staff_name = staffName,
                            to_date = weekEndDate.toString()
                        )
                    )

                    println("weekly main1 ${state.weeklyReportList}")
                }
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
            navController.navigate(Screen.HomeScreen.route)
        }, onIconClick = {})

        //WeekStartEndDates()

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_5)))

        // Use a Box to add the plus icon at the bottom right
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.state.value.isLoading.not()) {
                // Use LazyColumn to display the list of cards
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(leaveList1) { item ->
                        ReportCard(item = item)
                    }
                }
            } else {
                ShimmerEffectListView(isLoading = viewModel.state.value.isLoading)
            }

            ExpandableFAB(taskReport = { showEmpReport = true },
                qaReport = { qaReport = true },
                summary = { showSummaryReport = true },
                weeklyReport = { weeklyReport = true },
                monthlyReport = {
                    monthlyReport = true
                    if (userRole == "ADMIN") {
                        viewModel.getWeeklyReport(
                            StaffTaskDateWiseRequest(
                                from_date = monthStartDate.toString(),
                                staff_name = "ALL",
                                to_date = monthEndDate.toString()
                            )
                        )
                        taskViewModel.getStaffTaskDateWise(
                            StaffTaskDateWiseRequest(
                                from_date = monthStartDate.toString(),
                                staff_name = "ALL",
                                to_date = monthEndDate.toString()
                            )
                        )
                    }
                })
        }
    }

    if (showEmpReport) {
        EmployeeWiseReport(userRole = userRole!!, staffName = staffName!!,
            context = context, onClickCancelBtn = { showEmpReport = false })
    }

    if (showSummaryReport) {
        SummaryReport(viewModel = taskViewModel, summaryEnable = true, onClick = {
            showSummaryReport = false
        })
    }

    if (qaReport) {
        ToastMessage(context = context, message = "QA Report under development!!")
        qaReport = false
    }

    if (weeklyReport) {

        val raw = ReportModel(
            taskId = "",
            taskNo = "",
            taskName = "",
            resourceName = "",
            timeLine = 0.00,
            completionStatus = "",
            qaAcceptance = "",
            tlAcceptance = "",
            notes = ""
        )
        val reportList: MutableList<ReportModel?> = mutableListOf(raw)

        reportList.clear()

        println("weekly main ${state.weeklyReportList}")

        state.weeklyReportList?.forEach {
            val newReport = it.taskId?.let { it1 ->
                it.TaskNo?.let { it2 ->
                    ReportModel(
                        taskId = it1, taskNo = it2, taskName = it.taskName,
                        resourceName = it.resourceName, timeLine = it.sum_timeTaken.toDouble(),
                        completionStatus = it.latest_completed_level,
                        qaAcceptance = it.QA_Acceptance, tlAcceptance = it.TL_Acceptance,
                        notes = it.task_no
                    )
                }
            }
            reportList += newReport
        }

        println("weekly new $reportList")
        //println("list data {$taskState.staffTaskDateWise}")

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                ToolBar(nameOfScreen = "Weekly Report", iconOfScreen = 0, onClick = {
                    navController.navigate(Screen.ReportScreen.route)
                }, onIconClick = {})
                Spacer(modifier = Modifier.width(20.dp))

//                Box( modifier = Modifier.fillMaxWidth()
//                    .padding(horizontal = 20.dp)){
//                    YearWeekDropdown()
//                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Start Date:${convertDate(weekStartDate.toString())}",
                        style = TextStyle_500_12,
                        color = GraphColor
                    )
                    Text(
                        text = "End Date:${convertDate(weekEndDate.toString())}",
                        style = TextStyle_500_12,
                        color = GraphColor
                    )
                }
                //Spacer(modifier = Modifier.height(250.dp))
                GjWeeklyReport(reportList)
            }

        }
        //ToastMessage(context = context, message = "Weekly report under development!!")
        //weeklyReport = false
    }


    if (monthlyReport) {

        val raw = ReportModel(
            taskId = "",
            taskNo = "",
            taskName = "",
            resourceName = "",
            timeLine = 0.00,
            completionStatus = "",
            qaAcceptance = "",
            tlAcceptance = "",
            notes = ""
        )
        val reportList: MutableList<ReportModel?> = mutableListOf(raw)

        reportList.clear()
        state.weeklyReportList?.forEach {
            val newReport = it.taskId?.let { it1 ->
                it.TaskNo?.let { it2 ->
                    ReportModel(
                        taskId = it1, taskNo = it2, taskName = it.taskName,
                        resourceName = it.resourceName, timeLine = it.sum_timeTaken.toDouble(),
                        completionStatus = it.latest_completed_level,
                        qaAcceptance = it.QA_Acceptance, tlAcceptance = it.TL_Acceptance,
                        notes = it.task_no
                    )
                }
            }
            reportList += newReport
        }
        println("list data {$taskState.staffTaskDateWise}")

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                ToolBar(nameOfScreen = "Monthly Report", iconOfScreen = 0, onClick = {
                    navController.navigate(Screen.ReportScreen.route)
                }, onIconClick = {})
                Spacer(modifier = Modifier.width(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Start Date:${convertDate(monthStartDate.toString())}",
                        style = TextStyle_500_12,
                        color = GraphColor
                    )
                    Text(
                        text = "End Date:${convertDate(monthEndDate.toString())}",
                        style = TextStyle_500_12,
                        color = GraphColor
                    )
                }

                GjMonthlyReport(reportList)
            }

        }
        //ToastMessage(context = context, message = "Weekly report under development!!")
        //weeklyReport = false
    }
}

@Composable
fun ExpandableFAB(
    taskReport: () -> Unit,
    qaReport: () -> Unit,
    summary: () -> Unit,
    weeklyReport: () -> Unit,
    monthlyReport: () -> Unit
) {
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
                ReportItem("Task Report") {
                    taskReport.invoke()
                    isExpanded = false
                }
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("Summary") {
                    summary.invoke()
                    isExpanded = false
                }
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("QA Report") {
                    qaReport.invoke()
                    isExpanded = false
                }
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("Weekly Report") {
                    weeklyReport.invoke()
                    isExpanded = false
                }
                Spacer(modifier = Modifier.height(20.dp))
                ReportItem("Monthly Report") {
                    monthlyReport.invoke()
                    isExpanded = false
                }


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
fun ReportItem(reportName: String, onClick: () -> Unit) {
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

@Composable
fun ReportCard(item: RecentUpdateItem) {
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
                        if (item.leave_status == "0" && item.leave_status.isNotBlank()) {
                            Color.White
                        } else {
                            todoColor
                        }
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (item.leave_status == "0" && item.leave_status.isNotBlank()) {
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
                        item.jira_no?.let {
                        Text(
                            text = "Jira No",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))

                            Text(
                                text = it,
                                style = TextStyle_500_12,
                                color = ColorPrimary
                            )
                        }
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

                        if(item.timeTaken!!.isNotEmpty()){
                            Text(
                                text = if (item.timeTaken.isEmpty()
                                        .not()
                                ) {
                                    formatTime(item.timeTaken.toDouble())
                                } else {
                                    ""
                                },
                                style = TextStyle_500_12,
                                color = ColorPrimary
                            )
                        }else{
                            Text(
                                text = "No data",
                                style = TextStyle_500_12,
                                color = ColorPrimary
                            )
                        }


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
                            text = item.completed_level!! + " % done",
                            style = TextStyle_500_12,
                            color = if (item.task_status == "IN PROGRESS") {
                                Color.Red
                            } else {
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
                            text = "Name",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = item.staff_name!!,
                            style = TextStyle_500_12,
                            color = Color.Red
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
                        item.task_status?.let {
                            Text(
                                text = it,
                                style = TextStyle_500_12,
                                color = if (item.task_status == "COMPLETED") {
                                    ColorPrimary
                                } else {
                                    Color.Red
                                }
                            )
                        }
                    }
                } else {

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
                        item.leave_details?.let {
                            Text(
                                text = it,
                                style = TextStyle_500_12,
                                color = ColorPrimary
                            )
                        }
                    }

                }
            }
        }
    }
}

fun formatTime(fractionalHours: Double): String {
    val hours = fractionalHours.toInt()
    val minutes = ((fractionalHours - hours) * 60).toInt()

    return if (hours > 0 && minutes > 0) {
        "$hours hours $minutes minutes"
    } else if (hours > 0) {
        "$hours hours"
    } else {
        "$minutes minutes"
    }
}


