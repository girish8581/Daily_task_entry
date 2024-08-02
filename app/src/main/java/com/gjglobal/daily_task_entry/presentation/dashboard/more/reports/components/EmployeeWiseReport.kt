package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.gjglobal.daily_task_entry.BuildConfig
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.stafftaskdatewise.TaskDataDateWise
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.FormatTime
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.ReportViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.formatTime
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BgBlur
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.GraphColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_16
import com.gjglobal.daily_task_entry.presentation.utils.convertDate
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApiReport
import com.gjglobal.daily_task_entry.presentation.utils.fileTime
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmployeeWiseReport(
    onClickCancelBtn: (() -> Unit),userRole:String,staffName:String,context: Context
) {
    val viewModel: ReportViewModel = hiltViewModel()
    val taskViewModel :TaskAssignViewModel = hiltViewModel()
    val state = viewModel.state.value
    val taskState = taskViewModel.state.value


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
                            taskViewModel.isValidTaskList(false)
                        }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Task Report",
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
                            text = staffName,
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
                    
                    Spacer(modifier = Modifier.width(20.dp))

                    Button(
                        onClick = {
                            if (selectedItem.isNotEmpty()) {

                                createExcelReport(context = context, reportList = taskState.staffTaskDateWise!!)

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
                        if (taskState.isExporting.not()) {
                            androidx.compose.material3.Text(
                                text = "Export",
                                color = Color.White,
                                style = TextStyle_500_14
                            )
                        }
                        AnimatedVisibility(visible = taskState.isExporting) {
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


        val timeTaken = viewModel.state.value.staffTaskDateWise
            ?.map { formatTimeActual(it.timeTaken.toDouble()) }
            ?.filterNotNull()
            ?: emptyList()

        println(timeTaken)

        val sumOfTimeTaken = timeTaken
            .map { parseDuration(it) }
            .reduce { acc, duration -> acc.plus(duration) }

        println("sum:$sumOfTimeTaken")

        val formattedSum = formatDuration(sumOfTimeTaken)

        //println("formatted:$formattedSum")

        // Function to parse time strings like "06:30" to Duration


        val completedCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "In QA Testing" } ?: 0

        val inProgressCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "IN PROGRESS" } ?: 0




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
                RowWithBorder("Done Tasks ",completedCount.toString())
                RowWithBorder("In Progress Tasks ",inProgressCount.toString())
                RowWithBorder("Time Line ",formattedSum.toString())

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

                                            if(item.timeTaken.isNotEmpty()){
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
                                                item.timeTaken.let {
                                                    Text(
                                                        text = "$it Hrs",
                                                        style = TextStyle_500_12,
                                                        color = ColorPrimary
                                                    )
                                                }
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

                                        item.completed_level?.let {
                                            Text(
                                                text = "$it%",
                                                style = TextStyle_500_12,
                                                color = if(item.task_status=="IN PROGRESS"){
                                                    Color.Red}else{
                                                    DarkGreenColor
                                                }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekStartEndDates1() {
    val currentDate = LocalDate.now()
    val dayOfWeek = currentDate.dayOfWeek.value // 1 for Monday, 7 for Sunday

    // Calculate the start of the week based on the day of the week
    val weekStartDate = currentDate.minusDays((dayOfWeek - 1).toLong())

    // Calculate the end of the week (Saturday)
    val daysUntilEndOfWeek = DayOfWeek.SATURDAY.value - dayOfWeek
    val weekEndDate = weekStartDate.plusDays(daysUntilEndOfWeek.toLong())

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") // Format pattern
    val formattedStartDate = weekStartDate.format(dateFormatter)
    val formattedEndDate = weekEndDate.format(dateFormatter)

    Text("Current Date: $currentDate")
    Text("Start of the Week (Sunday): $formattedStartDate")
    Text("End of the Week (Saturday): $formattedEndDate")
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
@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryReport(viewModel: TaskAssignViewModel,onClick: () -> Unit,summaryEnable:Boolean) {

    val selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val weekStartDate = selectedDate.with(DayOfWeek.MONDAY)
    val weekEndDate = selectedDate.with(DayOfWeek.SUNDAY)

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") // Format pattern
    val formattedStartDate = weekStartDate.format(dateFormatter)
    val formattedEndDate = weekEndDate.format(dateFormatter)

//    Text("Current Date: $currentDate")
//    Text("Start of the Week (Sunday): $formattedStartDate")
//    Text("End of the Week (Saturday): $formattedEndDate")

    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val userRole = userData?.userType



    Card(
        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.dimen_10)),
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.dimen_10)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
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
                        viewModel.isValidTaskList(false)
                        onClick.invoke()
                    }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Weekly Summary Report",
                    color = Color(0xFF344767),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

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


        val timeTaken = viewModel.state.value.staffTaskDateWise
            ?.map { formatTimeActual( it.timeTaken.toDouble()) }
            ?.filterNotNull()
            ?: emptyList()

        println(timeTaken)

        val sumOfTimeTaken = timeTaken
            .map { parseDuration(it) }
            .reduce { acc, duration -> acc.plus(duration) }
//
//        println("sum:$sumOfTimeTaken")
//
        val formattedSum = formatDuration(sumOfTimeTaken)

        //println("formatted:$formattedSum")

        // Function to parse time strings like "06:30" to Duration


        val completedCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "In QA Testing" } ?: 0

        val inProgressCount = viewModel.state.value.staffTaskDateWise
            ?.groupBy { it.task_no }
            ?.values
            ?.map { it.last().task_status }
            ?.count { it == "IN PROGRESS" } ?: 0
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {



                    val data = listOf(
                        "Project Count" to projCount,
                        "Worked day Count" to daysCount,
                        "Resource Count" to staffList.size,
                        // Add more data points here
                    )

                    val data1 = listOf(
                        1f,
                       1.5f,
                        0.56f,
                        // Add more data points here
                    )

//                    Column {
//                        // Other Composables
//                        BarChart(modifier = Modifier,data1,200.dp)
//                    }


                    Column() {

                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()){
                            Text(text = "Start Date:${convertDate(weekStartDate.toString())}",style = TextStyle_500_12, color = GraphColor)
                            Text(text = "End Date:${convertDate(weekEndDate.toString())}", style = TextStyle_500_12,color = GraphColor)
                        }

                        BarChart(
                            modifier = Modifier.fillMaxWidth(),
                            values = listOf(projCount.toFloat(),taskNameCount.toFloat(), taskNamesCount.toFloat(),
                                completedCount.toFloat(),inProgressCount.toFloat(),
                                convertTimeToFloat(formattedSum)),
                            maxHeight = 200.dp,
                            labels =listOf( "Proj","Job","Task","Done","In prog","Time")
                        )

                        //println(convertTimeToFloat(formattedSum))

                        if(summaryEnable) {
                            Spacer(modifier = Modifier.height(20.dp))

                            RowWithBorder("Project Count ", projCount.toString())
                            RowWithBorder("Worked day Count ", daysCount.toString())
                            RowWithBorder("Resource Count ", staffList.size.toString())
                            RowWithBorder("Job Done ", taskNameCount.toString())
                            RowWithBorder("Total Tasks ", taskNamesCount.toString())
                            RowWithBorder("Done Tasks ", completedCount.toString())
                            RowWithBorder("In Progress Tasks ", inProgressCount.toString())
                            RowWithBorder("Time Line ", formattedSum.toString())
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }


                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryReportGraph(viewModel: TaskAssignViewModel,
                       onClick: () -> Unit,userRole:
                       String,staffName: String,
                       weekStartDate:String,weekEndDate:String) {

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") // Format pattern
    val formattedStartDate = weekStartDate.format(dateFormatter)
    val formattedEndDate = weekEndDate.format(dateFormatter)

    Box(){
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {

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


                val timeTaken = viewModel.state.value.staffTaskDateWise
                    ?.map { formatTimeActual( it.timeTaken.toDouble()) }
                    ?.filterNotNull()
                    ?: emptyList()

                println(timeTaken)

                val sumOfTimeTaken = timeTaken
                    .map { parseDuration(it) }
                    .reduce { acc, duration -> acc.plus(duration) }

                println("sum:$sumOfTimeTaken")

                val formattedSum = formatDuration(sumOfTimeTaken)

                println("formatted:$formattedSum")

                // Function to parse time strings like "06:30" to Duration


                val completedCount = viewModel.state.value.staffTaskDateWise
                    ?.groupBy { it.task_no }
                    ?.values
                    ?.map { it.last().task_status }
                    ?.count { it == "In QA Testing" } ?: 0

                val inProgressCount = viewModel.state.value.staffTaskDateWise
                    ?.groupBy { it.task_no }
                    ?.values
                    ?.map { it.last().task_status }
                    ?.count { it == "IN PROGRESS" } ?: 0
                Box(
                    Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    val data = listOf(
                        "Project Count" to projCount,
                        "Worked day Count" to daysCount,
                        "Resource Count" to staffList.size,
                        // Add more data points here
                    )

                    val data1 = listOf(
                        1f,
                        1.5f,
                        0.56f,
                        // Add more data points here
                    )

                    Column() {

                        BarChart(
                            modifier = Modifier.fillMaxWidth(),
                            values = listOf(projCount.toFloat(),taskNameCount.toFloat(), taskNamesCount.toFloat(),
                                completedCount.toFloat(),inProgressCount.toFloat(),
                                convertTimeToFloat(formattedSum)),
                            maxHeight = 100.dp,
                            labels =listOf( "Proj","Job","Task","Done","In prog","Time")
                        )

                    }
                }
            }
        }
    }


}


fun convertTimeToFloat(timeString: String): Float {
    val decimalFormat = DecimalFormat("0.00")
    return try {
        val parts = timeString.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val totalMinutes = hours * 60 + minutes
        val hoursFloat = totalMinutes / 60.0f

        decimalFormat.format(hoursFloat).toFloat()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        -1.0f // or any appropriate error value
    } catch (e: ArrayIndexOutOfBoundsException) {
        e.printStackTrace()
        -1.0f
    }
}


fun convertTimeToFloat2(timeString: String): Float {
    return try {
        val parts = timeString.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val totalMinutes = hours * 60 + minutes
        val hoursFloat = totalMinutes / 60.0f

        // Format the result to have two decimal points
        String.format("%.2f", hoursFloat).toFloat()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        -1.0f // or any appropriate error value
    } catch (e: ArrayIndexOutOfBoundsException) {
        e.printStackTrace()
        -1.0f
    }
}


@Composable
fun BarChart2(
    data: List<Pair<String, Int>>,
    maxValue: Int
) {
    val barWidth = 40f // Width of each bar in pixels
    val maxBarHeight = 150f // Maximum height of a bar in pixels
    val padding = 16f // Padding in pixels

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding.dp)
    ) {
        val barCount = data.size
        val barSpacing = (size.width - 2 * padding) / (barCount + 1)

        val maxDataValue = data.maxOf { it.second }.toFloat()

        data.forEachIndexed { index, (label, value) ->
            val barHeight = (value.toFloat() / maxValue) * maxBarHeight
            val x = padding + (index + 1) * barSpacing + index * barWidth
            val y = size.height - padding

            drawRect(
                color = Color.Blue,
                topLeft = Offset(x, y - barHeight),
                size = Size(barWidth, barHeight)
            )

            // Draw the label text using drawText
//            drawText(
//                text = label,
//                color = Color.Black,
//                fontSize = 16.sp,
//                topLeft = Offset(x + barWidth / 2, y + 20f)
//            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDuration(timeStr: String): Duration {
    val parts = timeStr.split(":")
    val hours = parts[0].toLong()
    val minutes = parts[1].toLong()
    return Duration.ofHours(hours).plusMinutes(minutes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDuration(duration: Duration): String {
    val totalMinutes = duration.toMinutes()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return String.format("%02d:%02d", hours, minutes)
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
                text = if(Name != "Time Line ") {"$Count Nos."}else{"$Count Hrs."},
                style = TextStyle_600_16,
                color = Color.White
            )
        }
    }
}

private val defaultMaxHeight = 100.dp
@Composable
internal fun BarChart1(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp = defaultMaxHeight
) {
    assert(values.isNotEmpty()) { "Input values are empty" }

    val borderColor = MaterialTheme.colors.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    // draw Y-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            Bar(
                value = item,
                color = MaterialTheme.colors.primary,
                maxHeight = maxHeight
            )
        }
    }

}

@Composable
internal fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    labels: List<String>, // List of labels for each bar
    maxHeight: Dp = defaultMaxHeight
) {
    assert(values.isNotEmpty() && values.size == labels.size) { "Input values are empty or labels don't match" }

    val borderColor = MaterialTheme.colors.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    // draw Y-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = labels[index], // Display label
                    style = TextStyle(
                        color = Color.Gray, // Customize label text color
                        fontSize = 12.sp // Customize label text size
                    )
                )

                Text(
                    text = item.toString(), // Display value
                    style = TextStyle(
                        color = Color.Black, // Customize value text color
                        fontSize = 12.sp // Customize value text size
                    )
                )
            }
            Bar(
                value = item,
                color = GraphColor,
                maxHeight = maxHeight
            )
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {

    val itemHeight = remember(value) { value * maxHeight.value / 100 }

    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color)
    )

}

private fun createExcelReport(context: Context, reportList: List<TaskDataDateWise>) {
    // Create a new Excel workbook
    val workbook = XSSFWorkbook()
    // Create a new sheet in the workbook
    val sheet = workbook.createSheet("WeeklyReport")
    // Add some data to the sheet
    val headers = arrayOf("DATE", "EMP NAME","PROJECT","RESOURCE NAME",
        "JIRA ID", "START","END","TIME (IN HRS)","TASK","JOB DONE","STATUS")

    // Add the headers to the first row
    val headerRow = sheet.createRow(0)
    headerRow.heightInPoints = 40f
    for (i in headers.indices) {
        val cell = headerRow.createCell(i)
        cell.setCellValue(headers[i])
    }

    // Add the data to subsequent rows
    for (i in reportList.indices) {
        val dataRow = sheet.createRow(i + 1)

        val taskDate = dataRow.createCell(0)
        val empNAME = dataRow.createCell(1)
        val projectName = dataRow.createCell(2)
        val taskName = dataRow.createCell(3)
        val jiraID = dataRow.createCell(4)
        val startTime = dataRow.createCell(5)
        val endTime = dataRow.createCell(6)
        val timeLine = dataRow.createCell(7)
        val task = dataRow.createCell(8)
        val jobDone = dataRow.createCell(9)
        val status = dataRow.createCell(10)


        taskDate.setCellValue(reportList[i].date)
        empNAME.setCellValue(reportList[i].staff_name)
        projectName.setCellValue(reportList[i].project_name)
        taskName.setCellValue(reportList[i].task_no)
        jiraID.setCellValue(reportList[i].jira_no)
        startTime.setCellValue(reportList[i].start_time.toString())
        endTime.setCellValue(reportList[i].end_time.toString())
        timeLine.setCellValue(reportList[i].timeTaken.toString())
        task.setCellValue(reportList[i].task_details)
        jobDone.setCellValue(reportList[i].job_done)
        status.setCellValue(reportList[i].task_status)

    }

    // Write the workbook to a file
    val file = File(context.getExternalFilesDir(null), "WeeklyReport"+ fileTime())
    val outputStream = FileOutputStream(file)
    workbook.write(outputStream)
    outputStream.close()

    // Open the Excel with a Excel viewer app
    val intent = Intent(Intent.ACTION_VIEW)
    val photoURI = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + context.getString(R.string.provider),
        file
    )
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(photoURI, context.getString(R.string.excel))
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }

}

data class ReportDailyTaskModel(
    val date:String, val employeeName:String, val project:String,
    val taskName:String,
    val resourceName:String, val jiraId:Double,
    val startTime:String, val endTime: String?, val timeLine: String?,
    val task:String,val jobDone:String,val status:String)

//    val headers = arrayOf("DATE", "EMP NAME","PROJECT","RESOURCE NAME",
//        "JIRA ID", "START","END","TIME (IN HRS)","TASK","JOB DONE","STATUS")

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

fun formatTimeActual(fractionalHours: Double): String {
    val hours = fractionalHours.toInt()
    val minutes = ((fractionalHours - hours) * 60).toInt()

    return if (hours > 0 && minutes > 0) {
        "$hours:$minutes"
    } else if (hours > 0) {
        "$hours:00"
    } else {
        "00:$minutes"
    }
}
