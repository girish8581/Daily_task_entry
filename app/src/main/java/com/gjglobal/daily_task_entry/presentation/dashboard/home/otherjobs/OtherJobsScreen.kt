package com.gjglobal.daily_task_entry.presentation.dashboard.home.otherjobs

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.AddNewOtherJobRequest
import com.gjglobal.daily_task_entry.domain.domain.model.otherjob.OtherJobData
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToastMessage
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.task.TaskViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDate
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OtherJobsScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: OtherJobViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val state = viewModel.state.value
    val taskListViewModel: TaskListViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()

    val textOtherJobDetails = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var showSuccess by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var selectedDate: String = ""
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select") }

    var expandedProject by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf("Select") }

    var expandedHours by remember { mutableStateOf(false) }
    var selectedHours by remember { mutableStateOf("0") }

    var expandedMinutes by remember { mutableStateOf(false) }
    var selectedMinutes by remember { mutableStateOf("0") }

    val listHourItems =
        ArrayList(listOf("0","1","2","3","4","5","6","7","8","10","12"))

    val listMinuteItems =
        ArrayList(listOf("0","0.25","0.5","0.75"))

    val listStatusItems =
        ArrayList(listOf("Daily scrum","Documentation/Reports","" +
                "KT Sessions","Team Discussions","Data Loading","Upskilling","Flow Checking","Demo","Meeting","Site Visit"))

    val pYear: Int
    val pMonth: Int
    val pDay: Int
    val calendar = Calendar.getInstance()
    pYear = calendar.get(Calendar.YEAR)
    pMonth = calendar.get(Calendar.MONTH)
    pDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val date = remember { mutableStateOf(currentDateApi()) }
    var sYear: String
    var sMonth: String
    var sDay: String

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            sYear = year.toString()
            sMonth = (month + 1).toString()
            Log.e("month", month.toString())
            sDay = dayOfMonth.toString()

            if (sMonth.length == 1) sMonth = "0$sMonth"
            if (sDay.length == 1) sDay = "0$sDay"

            date.value = "$sYear-$sMonth-$sDay"
        }, pYear, pMonth, pDay
    )

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                taskViewModel.getProjects()

                if (staffName != null) {
                    viewModel.getOtherJob(staffName = staffName)
                }
                //viewModel.getLeaveList(leaveListRequest = LeaveListRequest(staff_name = staffName!!))
                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }

    var listProjectItems = taskViewModel.state.value.projectList
        ?.map { it.project_name }
        ?.distinct()
        ?.toMutableList() ?: mutableListOf()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            ToolBar(nameOfScreen = "Other Jobs Entry", iconOfScreen = 0, onClick = {
                navController.popBackStack()
            }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column {
                            Spacer(modifier = Modifier.height(15.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = dimensionResource(id = R.dimen.dimen_10),
                                        horizontal = dimensionResource(id = R.dimen.dimen_20)
                                    ),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_15)),
                            ) {
                                Column(
                                    modifier = Modifier.background(lightestBlue)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column() {
                                            Spacer(modifier = Modifier.height(15.dp))
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column() {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "Select Date", style = TextStyle_600_14,
                                                            modifier = Modifier.width(110.dp),
                                                            color = ColorPrimary
                                                        )
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                        ) {
                                                            selectedDate = date.value
                                                            Box(
                                                                modifier = Modifier
                                                                    .border(
                                                                        0.5.dp,
                                                                        color = ColorPrimary,
                                                                        shape = RoundedCornerShape(
                                                                            4.27.dp
                                                                        )
                                                                    )
                                                                    .width(100.dp)
                                                                    .height(30.dp)
                                                            ) {
                                                                Text(
                                                                    text = formatDate(selectedDate).ifEmpty {
                                                                        currentDate()
                                                                    },
                                                                    style = TextStyle_400_14,
                                                                    modifier = Modifier.padding(5.dp)
                                                                )
                                                            }

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
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp)
                                                    ) {
                                                        Text(
                                                            text = "Select Project",
                                                            style = TextStyle_600_14,
                                                            color = ColorPrimary,
                                                            modifier = Modifier.width(110.dp)
                                                        )

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
                                                            ExposedDropdownMenuBox(
                                                                expanded = expandedProject,
                                                                onExpandedChange = {
                                                                    listProjectItems.clear()
                                                                    listProjectItems = taskListViewModel.state.value.taskList
                                                                        ?.map { it.project_name }
                                                                        ?.distinct()
                                                                        ?.toMutableList() ?: mutableListOf()

                                                                    expandedProject = !expandedProject
                                                                }) {
                                                                ExposedDropdownMenu(expanded = expandedProject,
                                                                    onDismissRequest = {
                                                                        expandedProject = false
                                                                    }) {
                                                                    listProjectItems.forEach { selectedOption ->
                                                                        DropdownMenuItem(onClick = {
                                                                            selectedProject = selectedOption

                                                                            expandedProject = false
                                                                        }) {
                                                                            Text(
                                                                                text = selectedOption,
                                                                                style = TextStyle_400_14,
                                                                                fontWeight = if (selectedOption == selectedProject) FontWeight.Bold else null,
                                                                                color = if (selectedOption == selectedProject) Color.Red else Color.Black
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Row(
                                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    modifier = Modifier
                                                                        .padding(
                                                                            start = 8.dp,
                                                                            end = 15.dp
                                                                        )
                                                                        .fillMaxSize()
                                                                        .clickable {
                                                                            expandedProject = true
                                                                        },

                                                                    ) {
                                                                    Text(
                                                                        text = selectedProject,
                                                                        color = ColorPrimary,
                                                                        style = TextStyle_400_12
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

                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp)
                                                    ) {
                                                        Text(
                                                            text = "Select Job",
                                                            style = TextStyle_600_14,
                                                            color = ColorPrimary,
                                                            modifier = Modifier.width(110.dp)
                                                        )
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
                                                            ExposedDropdownMenuBox(
                                                                expanded = expandedStatus,
                                                                onExpandedChange = {
                                                                    expandedStatus = !expandedStatus
                                                                }) {
                                                                ExposedDropdownMenu(expanded = expandedStatus,
                                                                    onDismissRequest = { expandedStatus = false }) {
                                                                    listStatusItems.forEach { selectedOption ->
                                                                        DropdownMenuItem(onClick = {
                                                                            selectedStatus = selectedOption
                                                                            expandedStatus = false
                                                                        }) {
                                                                            Text(
                                                                                text = selectedOption,
                                                                                style = TextStyle_400_14,
                                                                                fontWeight = if (selectedOption == selectedStatus) FontWeight.Bold else null,
                                                                                color = if (selectedOption == selectedStatus) Color.Red else Color.Black
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Row(
                                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    modifier = Modifier
                                                                        .padding(
                                                                            start = 8.dp,
                                                                            end = 15.dp
                                                                        )
                                                                        .fillMaxSize()
                                                                        .clickable {
                                                                            expandedStatus = true
                                                                        },

                                                                    ) {
                                                                    Text(
                                                                        text = selectedStatus,
                                                                        color = ColorPrimary,
                                                                        style = TextStyle_400_12
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

                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp)
                                                    ) {
                                                        Text(
                                                            text = "Select Hours",
                                                            style = TextStyle_600_14,
                                                            color = ColorPrimary,
                                                            modifier = Modifier.width(110.dp)
                                                        )
                                                        Box(
                                                            modifier = Modifier
                                                                .border(
                                                                    0.5.dp,
                                                                    color = ColorPrimary,
                                                                    shape = RoundedCornerShape(4.27.dp)
                                                                )
                                                                .width(70.dp)
                                                                .height(35.dp)
                                                        ) {
                                                            ExposedDropdownMenuBox(
                                                                expanded = expandedHours,
                                                                onExpandedChange = {
                                                                    expandedHours = !expandedHours
                                                                }) {
                                                                ExposedDropdownMenu(expanded = expandedHours,
                                                                    onDismissRequest = { expandedHours = false }) {
                                                                    listHourItems.forEach { selectedOption ->
                                                                        DropdownMenuItem(onClick = {
                                                                            selectedHours = selectedOption
                                                                            expandedHours = false
                                                                        }) {
                                                                            Text(
                                                                                text = selectedOption,
                                                                                style = TextStyle_400_14,
                                                                                fontWeight = if (selectedOption == selectedHours) FontWeight.Bold else null
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Row(
                                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    modifier = Modifier
                                                                        .padding(
                                                                            start = 8.dp,
                                                                            end = 15.dp
                                                                        )
                                                                        .fillMaxSize()
                                                                        .clickable {
                                                                            expandedHours = true
                                                                        },

                                                                    ) {
                                                                    Text(
                                                                        text = selectedHours,
                                                                        color = ColorPrimary,
                                                                        style = TextStyle_400_12
                                                                    )
                                                                    Spacer(modifier = Modifier.height(15.dp))
                                                                    Image(
                                                                        painter = painterResource(id = R.drawable.down_arrow),
                                                                        contentDescription = "down arrow"
                                                                    )
                                                                }
                                                            }
                                                        }

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
                                                            ExposedDropdownMenuBox(
                                                                expanded = expandedMinutes,
                                                                onExpandedChange = {
                                                                    expandedMinutes = !expandedMinutes
                                                                }) {
                                                                ExposedDropdownMenu(expanded = expandedMinutes,
                                                                    onDismissRequest = { expandedMinutes = false }) {
                                                                    listMinuteItems.forEach { selectedOption ->
                                                                        DropdownMenuItem(onClick = {
                                                                            selectedMinutes = selectedOption
                                                                            expandedMinutes = false
                                                                        }) {
                                                                            Text(
                                                                                text = selectedOption,
                                                                                style = TextStyle_400_14,
                                                                                fontWeight = if (selectedOption == selectedMinutes) FontWeight.Bold else null
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Row(
                                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    modifier = Modifier
                                                                        .padding(
                                                                            start = 8.dp,
                                                                            end = 15.dp
                                                                        )
                                                                        .fillMaxSize()
                                                                        .clickable {
                                                                            expandedMinutes = true
                                                                        },

                                                                    ) {
                                                                    Text(
                                                                        text = selectedMinutes,
                                                                        color = ColorPrimary,
                                                                        style = TextStyle_400_12
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

                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(5.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {

                                                        Box(
                                                            modifier = Modifier
                                                                .padding(horizontal = 20.dp)
                                                                .background(LightBlue)

                                                                .background(
                                                                    Color.White,
                                                                    shape = RoundedCornerShape(7.dp)
                                                                )
                                                                .border(
                                                                    BorderStroke(
                                                                        1.dp,
                                                                        ColorPrimary
                                                                    ),
                                                                    shape = RoundedCornerShape(7.dp)
                                                                )
                                                        ) {
                                                            Column(
                                                                verticalArrangement = Arrangement.Center,
                                                                modifier = Modifier.padding(
                                                                    dimensionResource(id = R.dimen.dimen_5)
                                                                )
                                                            ) {
                                                                Text(
                                                                    text = "Job details",
                                                                    color = TextColor,
                                                                    style = TextStyle_400_12,
                                                                    modifier = Modifier.align(Alignment.Start)
                                                                )
                                                                Spacer(
                                                                    modifier = Modifier.height(
                                                                        dimensionResource(
                                                                            id = R.dimen.dimen_2
                                                                        )
                                                                    )
                                                                )
                                                                BasicTextField(
                                                                    value = textOtherJobDetails.value,
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .align(Alignment.Start)
                                                                        .focusRequester(
                                                                            focusRequester
                                                                        ),
                                                                    onValueChange = {

                                                                        textOtherJobDetails.value = it
                                                                        //                 viewModel.isValidUsername(it)
                                                                    },
                                                                    textStyle = TextStyle_500_16,
                                                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                                                    keyboardOptions = KeyboardOptions(
                                                                        keyboardType = KeyboardType.Text
                                                                    )
                                                                )

                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = dimensionResource(id = R.dimen.dimen_15))
                                                .background(ColorPrimary)
                                                .fillMaxWidth()
                                                .height(dimensionResource(id = R.dimen.dimen_50))
                                                .clickable {

                                                    if (textOtherJobDetails.value.isNotEmpty() &&
                                                        selectedStatus != "Select" && selectedProject != "Select" && (selectedMinutes != "0" || selectedHours !="0")
                                                    ) {

                                                        val totalTime = selectedHours.toDouble() +selectedMinutes.toDouble()
                                                                viewModel.addNewOtherJob(
                                                            addNewOtherJobRequest = AddNewOtherJobRequest(
                                                                job_date = selectedDate,
                                                                project_name = selectedProject,
                                                                job_type = selectedStatus,
                                                                job_hours = totalTime.toString(),
                                                                job_details = textOtherJobDetails.value.toString(),
                                                                staff_name = staffName.toString()
                                                            ), onSuccess = {
                                                                focusManager.clearFocus()
                                                                showSuccess = true
                                                                textOtherJobDetails.value = ""
                                                                selectedStatus = "Select"
                                                                selectedProject = "Select"
                                                                selectedHours = "0"
                                                                selectedMinutes = "0"

                                                                if (staffName != null) {
                                                                    viewModel.getOtherJob(staffName = staffName)
                                                                }

                                                            }
                                                        )
                                                    } else {
                                                        showToast = true
                                                    }
                                                }, contentAlignment = Alignment.Center
                                        ) {

                                            if (state.isLoading.not()) {
                                                Text(
                                                    text = "Submit",
                                                    style = TextStyle_500_14,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(
                                                        top = dimensionResource(
                                                            id = R.dimen.dimen_3
                                                        )
                                                    )
                                                )
                                            }else {

                                                androidx.compose.material3.CircularProgressIndicator(
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                )

                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            state.otherJobList?.let { OtherJobListCard(it) }

                        }
                    }

        }
    }

    if(viewModel.state.value.error?.toString()?.isNotEmpty() == true){
        //ToastMessage(context,viewModel.state.value.error?.toString()!!)
    }

    if (showSuccess) {
        Messagebox(onSuccess = {
            showSuccess = false
            //onCancelClick.invoke()
        }, message = "Job added successfully!!")
    }

    if(showToast){
        ToastMessage(context = context, message = "Please fill all values!!" )
        showToast = false
    }

}

@Composable
fun OtherJobListCard(otherJobList: List<OtherJobData>){
            //val otherJobList by viewModel.otherJobList.collectAsState()
            if (otherJobList.isNotEmpty()) {
                Box(contentAlignment = Alignment.Center,
                modifier = Modifier.padding()) {
                    Column() {
                        Spacer(modifier = Modifier.height(20.dp))
                        LazyColumn(modifier = Modifier.height(400.dp)) {
                            items(otherJobList) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = dimensionResource(id = R.dimen.dimen_5),
                                            horizontal = dimensionResource(id = R.dimen.dimen_20)
                                        ),
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
                                    elevation = 5.dp
                                ){
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        ){

                                        Column {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.padding(horizontal = 10.dp)){
                                                item.actDate?.let {
                                                    Text(modifier = Modifier.width(100.dp), text = it,style = TextStyle_600_12,
                                                        color = ColorPrimary)
                                                }
                                                item.actType?.let {
                                                    Text(text = it,style = TextStyle_600_12,
                                                        color = DarkGreen)
                                                }
                                            }

                                            Row(horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(horizontal = 10.dp)){
                                                Text(modifier = Modifier.width(100.dp), text = "Project",style = TextStyle_600_12,
                                                    color = ColorPrimary)
                                                item.projectName?.let {
                                                    Text(text = it,style = TextStyle_600_12,
                                                        color = ColorPrimary)
                                                }
                                            }

                                            Row(horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(horizontal = 10.dp)){
                                                Text(modifier = Modifier.width(100.dp), text = "Name",style = TextStyle_600_12,
                                                    color = ColorPrimary)
                                                item.staffName?.let {
                                                    Text(text = it,style = TextStyle_600_12,
                                                        color = ColorPrimary)
                                                }
                                            }

                                            Row(horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(horizontal = 10.dp)){
                                                Text(modifier = Modifier.width(100.dp), text = "Time Spend",style = TextStyle_600_12,
                                                    color = ColorPrimary)
                                                item.actTime?.let {
                                                    Text(text = formatTime(it.toDouble()),style = TextStyle_600_12,
                                                        color = ColorPrimary)
                                                }
                                            }

                                            Row(horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(horizontal = 10.dp)){
                                                Text(modifier = Modifier.width(100.dp), text = "Job",style = TextStyle_600_12,
                                                    color = ColorPrimary)
                                                item.activity?.let {
                                                    Text(text = it,style = TextStyle_600_12,
                                                        color = ColorPrimary)
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

            }else{

                Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                    //if(viewModel.state.value.isLoading.not()) {
                        Box(
                            Modifier
                                .height(400.dp)
                                .clickable {}, contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
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
                   // }else{
//                        Text(
//                            text = "Loading...",
//                            style = TextStyle_400_14,
//                            modifier = Modifier.padding(top=100.dp)
//                        )
                   // }
                }

            }
        //}
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




