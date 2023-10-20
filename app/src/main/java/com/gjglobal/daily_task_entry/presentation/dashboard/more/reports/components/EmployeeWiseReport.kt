package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.FormatTime
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.ReportViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BgBlur
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_16
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApiReport
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmployeeWiseReport(
    onClickCancelBtn: (() -> Unit),
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val viewModel: ReportViewModel = hiltViewModel()
    val taskViewModel :TaskAssignViewModel = hiltViewModel()
    val state = viewModel.state.value
    val taskState = taskViewModel.state.value

    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val userRole = userData?.userType

    var selectedFromDate =""
    var selectedToDate =""
    var selectedUserId by remember {
        mutableStateOf("")
    }

    val pYear : Int
    val pMonth:Int
    val pDay:Int
    val calendar = Calendar.getInstance()
    pYear=calendar.get(Calendar.YEAR)
    pMonth = calendar.get(Calendar.MONTH)
    pDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf(currentDateApiReport()) }
    val date2 = remember { mutableStateOf(currentDateApiReport()) }

    var sYear:String
    var sMonth:String
    var sDay:String

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year:Int, month:Int, dayOfMonth:Int ->
            sYear = year.toString()
            sMonth = (month + 1).toString()
            Log.e("month",month.toString())
            sDay = dayOfMonth.toString()

            if (sMonth.length == 1) sMonth="0$sMonth"
            if (sDay.length == 1) sDay="0$sDay"

            date.value = "$sYear/$sMonth/$sDay"
        },pYear,pMonth,pDay
    )

    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    val datePickerDialog2 = DatePickerDialog(
        context,
        { _: DatePicker, year:Int, month:Int, dayOfMonth:Int ->
            sYear = year.toString()
            sMonth = (month + 1).toString()
            Log.e("month",month.toString())
            sDay = dayOfMonth.toString()

            if (sMonth.length == 1) sMonth="0$sMonth"
            if (sDay.length == 1) sDay="0$sDay"

            date2.value = "$sYear/$sMonth/$sDay"
        },pYear,pMonth,pDay
    )

    datePickerDialog2.datePicker.maxDate = System.currentTimeMillis()

    Log.e("fromDate",date.toString())

    var expanded by remember {
        mutableStateOf(false)
    }

    val listItems: ArrayList<String> = ArrayList()
    val listStaffIds: ArrayList<String> = ArrayList()


    var selectedItem by remember {
        mutableStateOf("ALL")
    }

    listItems.add("ALL")
    taskState.staffList?.forEach {
        listItems.add(it.staff_name)
        listStaffIds.add(it.id)
    }

    Box(
        Modifier
            .fillMaxHeight()
            .background(BgBlur),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.dimen_10)),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.dimen_10)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(5.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_round_cancel_24),
                    contentDescription = "roundCancel",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            onClickCancelBtn.invoke()
                        }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Employee wise Report",
                        color = Color(0xFF344767),
                        textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if(userRole=="ADMIN") {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Name:",
                            style = TextStyle_400_14,
                            modifier = Modifier.width(100.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .border(
                                    0.5.dp,
                                    color = ColorPrimary,
                                    shape = RoundedCornerShape(4.27.dp)
                                )
                                .fillMaxWidth()
                                .height(35.dp)
                        ) {
                            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
                                expanded = !expanded
                            }) {
                                ExposedDropdownMenu(expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    listItems.forEach { selectedOption ->
                                        DropdownMenuItem(onClick = {
                                            selectedItem = selectedOption
                                            val index = listItems.indexOf(selectedItem)
                                            selectedUserId =
                                                state.staffList?.get(index)?.id.toString()
                                            //viewModel.setStaffId(listStaffIds[index])
                                            Log.e(
                                                "userId",
                                                state.staffList?.get(index)?.id.toString()
                                            )
                                            expanded = false
                                        }) {
                                            Text(
                                                text = selectedOption,
                                                fontWeight = if (selectedOption == selectedItem) FontWeight.Bold else null
                                            )
                                        }
                                    }
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 15.dp)
                                        .fillMaxSize()
                                        .clickable {
                                            expanded = true
                                        },

                                    ) {
                                    Text(
                                        text = selectedItem,
                                        color = ColorPrimary,
                                        style = TextStyle_400_14
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.down_arrow),
                                        contentDescription = "down arrow"
                                    )
                                }
                            }
                        }
                    }
                }else{
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Name:",
                            style = TextStyle_400_14,
                            modifier = Modifier.width(120.dp)
                        )


                        Text(
                            text = staffName!!,
                            style = TextStyle_400_14
                        )
                        selectedItem = staffName
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "From Date : ", style = TextStyle_400_14,
                        modifier = Modifier.width(120.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        selectedFromDate = date.value
                        Text(text = selectedFromDate.ifEmpty {
                            currentDateApiReport()
                        }, style = TextStyle_400_14)
                        Image(
                            painter = painterResource(id = R.drawable.calender_icon),
                            contentDescription = "calender icon",
                            modifier = Modifier.clickable {
                                datePickerDialog.show()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "To Date : ", style = TextStyle_400_14,
                        modifier = Modifier.width(120.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        selectedToDate = date2.value
                        Text(text = selectedToDate.ifEmpty {
                            currentDateApiReport()
                        }, style = TextStyle_400_14)
                        Image(
                            painter = painterResource(id = R.drawable.calender_icon),
                            contentDescription = "calender icon",
                            modifier = Modifier.clickable {
                                datePickerDialog2.show()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = {
                            if (selectedItem.isNotEmpty()) {

                                println(selectedItem)
                                taskState.isTaskList = false
                                // taskState.staffTaskDateWise = null
                                taskViewModel.getStaffTaskDateWise(
                                    StaffTaskDateWiseRequest(
                                        from_date = selectedFromDate,
                                        staff_name = selectedItem,
                                        to_date = selectedToDate
                                    )
                                )
                            } else {
                                Toast.makeText(context, "Please select staff!!", Toast.LENGTH_LONG)
                                    .show()
                            }

                        },
                        colors = ButtonDefaults.buttonColors(ColorPrimary),
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (taskState.isLoading.not()) {
                            androidx.compose.material3.Text(
                                text = "Submit",
                                color = Color.White,
                                style = TextStyle_500_14
                            )
                        }
                        AnimatedVisibility(visible = taskState.isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }

               }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    color = ColorPrimary,
                    thickness = 1.dp
                )

                if (taskState.isLoading.not()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .clickable {}, contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ReportDateWiseCard(
                                viewModel = taskViewModel
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(visible = state.isLoading) {
                            CircularProgressIndicator(
                                color = ColorPrimary,
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.dimen_40))
                            )
                        }
                    }
                }
            }
            }
        }
    }




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportDateWiseCard(viewModel: TaskAssignViewModel) {
    var i = 0
    if (viewModel.state.value.isTaskList!!) {
        val listTaskNo: ArrayList<String> = ArrayList()
        val staffList: ArrayList<String> = ArrayList()


        viewModel.state.value.staffTaskDateWise?.let { staffTasks ->
            val taskNumbersSet = HashSet<String>()
            staffTasks.forEach { task ->
                task.task_no.let { taskNo ->
                    taskNumbersSet.add(taskNo)
                }
            }
            listTaskNo.addAll(taskNumbersSet)
        }

        viewModel.state.value.staffTaskDateWise?.let { staffTasks ->
            val taskNumbersSet = HashSet<String>()
            staffTasks.forEach { task ->
                task.staff_name.let { taskNo ->
                    taskNumbersSet.add(taskNo)
                }
            }
            staffList.addAll(taskNumbersSet)
        }
        val taskNames = viewModel.state.value.staffTaskDateWise
            ?.map { it.task_no }

        val taskNameCount = taskNames?.count() ?: 0

        val taskNamesList = viewModel.state.value.staffTaskDateWise
            ?.map { it.task_no }
            ?.distinct()

        val taskNamesCount = taskNamesList?.count() ?: 0

        val projList = viewModel.state.value.staffTaskDateWise
            ?.map { it.project_name }
            ?.distinct()

        val projCount = projList?.count() ?: 0

        val days = viewModel.state.value.staffTaskDateWise
            ?.map { it.date }
            ?.distinct()

        val daysCount = days?.count() ?: 0


//        val completedCount = viewModel.state.value.staffTaskDateWise
//            ?.filter { it.task_status == "COMPLETED" }
//            ?.groupBy { it.task_no }
//            ?.mapValues { it.value.count() }

//        val completedCount = viewModel.state.value.staffTaskDateWise
//            ?.filter { it.task_status == "COMPLETED" }
//            ?.map { it.task_no }
//            ?.distinct()
//            ?.count() ?: 0

        val completedCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "COMPLETED" } ?: 0

        val inProgressCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "IN PROGRESS" } ?: 0

//        val inProgressCount = viewModel.state.value.staffTaskDateWise
//            ?.filter { it.task_status == "IN PROGRESS" }
//            ?.map { it.task_no }
//            ?.distinct()
//            ?.count() ?: 0


        //val completeCount = completedCount?.count() ?: 0


//        val inProgress = viewModel.state.value.staffTaskDateWise
//        ?.filter { it.task_status == "IN PROGRESS" }
//            ?.groupBy { it.task_no }
//            ?.mapValues { it.value.count() }
//
//        val inProgressCount = inProgress?.count() ?: 0


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding()
        ) {
            Column() {

                RowWithBorder("Project Count ",projCount.toString())
                RowWithBorder("Worked day Count ",daysCount.toString())
                RowWithBorder("Resource Count ",staffList.size.toString())
                RowWithBorder("Job Done ",taskNameCount.toString())
                RowWithBorder("Total Tasks ",taskNamesCount.toString())
                RowWithBorder("Completed Tasks ",completedCount.toString())
                RowWithBorder("In Progress Tasks ",inProgressCount.toString())

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.height(500.dp)) {
                    items(viewModel.state.value.staffTaskDateWise!!) { item ->
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
                                    modifier = Modifier
                                        .background(
                                            Color.White
                                        )
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center
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
                                            text = item.project_name,
                                            style = TextStyle_600_12,
                                            color = Color.Red
                                        )

                                        Text(
                                            text = item.task_no,
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
                                                text = formatDate(item.date),
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
                                            text = "Name",
                                            style = TextStyle_600_12,
                                            color = ColorPrimary,
                                            modifier = Modifier.width(70.dp)
                                        )
                                        Spacer(modifier = Modifier.width(30.dp))
                                        Text(
                                            text = item.staff_name,
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
                                                text = item.task_details,
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
                                            val startTime = FormatTime(item.start_time)
                                            val endTime = FormatTime(item.end_time)
                                            Text(
                                                text = startTime + " to  " + endTime +""+ if(item.timeTaken.isNullOrEmpty().not()){" / "+ item.timeTaken+" Hrs"}else{""},
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
                                        }

//                                    Row(
//                                        horizontalArrangement = Arrangement.Center,
//                                        modifier = Modifier.padding(
//                                            horizontal = 10.dp,
//                                        )
//                                    ) {
//                                        Text(
//                                            text = "Level",
//                                            style = TextStyle_600_12,
//                                            color = ColorPrimary,
//                                            modifier = Modifier.width(100.dp)
//                                        )
//
//                                        Text(
//                                            text = item.,
//                                            style = TextStyle_500_12,
//                                            color = if(item.task_status=="IN PROGRESS"){
//                                                Color.Red}else{
//                                                DarkGreenColor
//                                            }
//                                        )
//                                    }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Jira ID",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.jira_no,
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
                                                text = item.job_done,
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
                                                text = item.task_status,
                                                style = TextStyle_500_12,
                                                color = if (item.task_status =="COMPLETED"){ColorPrimary}else{Color.Red}
                                            )
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

@Composable
fun RowWithBorder(Name:String,Count:String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .fillMaxWidth()
            .background(ColorPrimary)
            .border(0.5.dp, ColorPrimary) // Adjust border thickness and color as needed
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 2.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Name,
                style = TextStyle_600_16,
                color = Color.White
            )

            Text(
                text = "$Count Nos.",
                style = TextStyle_600_16,
                color = Color.White
            )
        }
    }
}

