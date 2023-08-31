package com.gjglobal.daily_task_entry.presentation.dashboard.more.task

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.task.AddNewTaskRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name

    val state = viewModel.state.value

    val jobType = remember { mutableStateOf("DEVP") }
    val taskName = remember { mutableStateOf("") }
    var clickDevp by remember { mutableStateOf(true) }
    var clickBugFix by remember { mutableStateOf(false) }
    var clickMeeting by remember { mutableStateOf(false) }
    var clickModification by remember { mutableStateOf(false) }
    var clickDemo by remember { mutableStateOf(false) }
    var clickImpl by remember { mutableStateOf(false) }
    var clickSkill by remember { mutableStateOf(false) }
    var clickVisit by remember { mutableStateOf(false) }
    val textJiraId = remember { mutableStateOf("") }
    val textProjectDetails = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var showSuccess by remember { mutableStateOf(false) }
    var startDate: String = ""
    var estimateDate: String = ""

    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select") }


    val pYear: Int
    val pMonth: Int
    val pDay: Int
    val calendar = Calendar.getInstance()
    pYear = calendar.get(Calendar.YEAR)
    pMonth = calendar.get(Calendar.MONTH)
    pDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val startDateCalender = remember { mutableStateOf(currentDateApi()) }
    val estimateDateCalender = remember { mutableStateOf(currentDateApi()) }
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

            startDateCalender.value = "$sYear-$sMonth-$sDay"
        }, pYear, pMonth, pDay
    )

    val estimateDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            sYear = year.toString()
            sMonth = (month + 1).toString()
            Log.e("month", month.toString())
            sDay = dayOfMonth.toString()

            if (sMonth.length == 1) sMonth = "0$sMonth"
            if (sDay.length == 1) sDay = "0$sDay"

            estimateDateCalender.value = "$sYear-$sMonth-$sDay"
        }, pYear, pMonth, pDay
    )


    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.getProjects()
                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }


    val listStatusItems =ArrayList<String>()

    state.projectList?.forEach {
        listStatusItems.add(it.project_name)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            ToolBar(nameOfScreen = "Add Task", iconOfScreen = 0, onClick = {
                navController.popBackStack()
            }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))


            Box(modifier = Modifier.fillMaxSize()) {
                Column() {
                    Spacer(modifier = Modifier.height(15.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = dimensionResource(id = R.dimen.dimen_10),
                                horizontal = dimensionResource(id = R.dimen.dimen_20)
                            ),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_20)),
                    ) {
                        Column(
                            modifier = Modifier.background(lightestBlue)
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column() {
                                    Spacer(modifier = Modifier.height(20.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp)
                                        ) {
                                            Text(
                                                text = "Select Project",
                                                style = TextStyle_600_14,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Spacer(modifier = Modifier.width(10.dp))
//                                            if(state.isProjectList!!.not()){
//                                                AnimatedVisibility(visible = state.isProjectList!!.not()) {
//                                                    CircularProgressIndicator(
//                                                        color = ColorPrimary,
//                                                        modifier = Modifier
//                                                            .size(20.dp)
//                                                    )
//                                                }
//                                            }else {
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
                                                            onDismissRequest = {
                                                                expandedStatus = false
                                                            }) {
                                                            listStatusItems.forEach { selectedOption ->
                                                                DropdownMenuItem(onClick = {
                                                                    selectedStatus = selectedOption
                                                                    state.isTaskCountData = false
                                                                    viewModel.getTaskCount(selectedStatus)
                                                                    expandedStatus = false
                                                                }) {
                                                                    Text(
                                                                        text = selectedOption,
                                                                        style = TextStyle_400_12,
                                                                        fontWeight = if (selectedOption == selectedStatus) FontWeight.Bold else null
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
                                            //}
                                        }


                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp)
                                    ) {
                                        Text(
                                            text = "Task No",
                                            style = TextStyle_600_14,
                                            color = ColorPrimary,
                                            modifier = Modifier.width(100.dp)
                                        )
                                        Spacer(modifier = Modifier.width(20.dp))

                                            if(state.isTaskCountLoding!!.not()){
                                                if(state.isTaskCountData!!) {
                                                    taskName.value =
                                                        state.taskCountData?.project_code!! + "-" + jobType.value + "-" + state.taskCountData?.task_no!!
                                                    Text(
                                                        text = state.taskCountData?.project_code!! + "-" + jobType.value + "-" + state.taskCountData?.task_no!!,
                                                        style = TextStyle_600_14,
                                                        color = Color.Red
                                                    )
                                                }
                                            }else{
                                                    AnimatedVisibility(visible = state.isTaskCountLoding!!) {
                                                        CircularProgressIndicator(
                                                            color = ColorPrimary,
                                                            modifier = Modifier
                                                                .size(20.dp)
                                                        )
                                                    }

                                            }

                                    }

                                    Spacer(modifier = Modifier.height(15.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Job Type",
                                            style = TextStyle_600_14,
                                            modifier = Modifier.width(100.dp),
                                            color = ColorPrimary
                                        )

                                        Box(modifier = Modifier
                                            .background(
                                                if (clickDevp) ColorPrimary else Color.White,
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .border(
                                                BorderStroke(1.dp, ColorPrimary),
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .clickable {
                                                clickDevp = true
                                                clickBugFix = false
                                                clickMeeting = false
                                                clickModification = false
                                                clickDemo = false
                                                clickImpl = false
                                                clickSkill = false
                                                clickVisit = false

                                                jobType.value = "DEVP"

                                            }
                                            .width(100.dp),
                                            contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "DEVP",
                                                style = TextStyle_500_12,
                                                color = if (clickDevp) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 5.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        Box(modifier = Modifier
                                            .background(
                                                if (clickBugFix) ColorPrimary else Color.White,
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .border(
                                                BorderStroke(1.dp, ColorPrimary),
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .clickable {
                                                clickDevp = false
                                                clickBugFix = true
                                                clickMeeting = false
                                                clickModification = false
                                                clickDemo = false
                                                clickImpl = false
                                                clickSkill = false
                                                clickVisit = false
                                                jobType.value = "BUGF"

                                            }
                                            .width(82.dp),
                                            contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "BUGF",
                                                style = TextStyle_400_12,
                                                color = if (clickBugFix) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 5.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 10.dp)
                                            ) {

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickMeeting) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        jobType.value = "MEET"
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = true
                                                        clickModification = false
                                                        clickDemo = false
                                                        clickImpl = false
                                                        clickSkill = false
                                                        clickVisit = false

                                                    }
                                                    .width(100.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "MEET",
                                                        style = TextStyle_500_12,
                                                        color = if (clickMeeting) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(20.dp))

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickModification) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        jobType.value = "MODF"
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = false
                                                        clickModification = true
                                                        clickDemo = false
                                                        clickImpl = false
                                                        clickSkill = false
                                                        clickVisit = false

                                                    }
                                                    .width(100.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "MODF",
                                                        style = TextStyle_500_12,
                                                        color = if (clickModification) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(20.dp))

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickDemo) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = false
                                                        clickModification = false
                                                        clickDemo = true
                                                        clickImpl = false
                                                        clickSkill = false
                                                        clickVisit = false
                                                        jobType.value = "DEMO"
                                                    }
                                                    .width(82.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "DEMO",
                                                        style = TextStyle_400_12,
                                                        color = if (clickDemo) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 10.dp)
                                            ) {

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickImpl) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        jobType.value = "IMPL"
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = false
                                                        clickModification = false
                                                        clickDemo = false
                                                        clickImpl = true
                                                        clickSkill = false
                                                        clickVisit = false

                                                    }
                                                    .width(100.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "IMPL",
                                                        style = TextStyle_500_12,
                                                        color = if (clickImpl) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(20.dp))

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickSkill) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        jobType.value = "SKIL"
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = false
                                                        clickModification = false
                                                        clickDemo = false
                                                        clickImpl = false
                                                        clickSkill = true
                                                        clickVisit = false

                                                    }
                                                    .width(100.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "SKIL",
                                                        style = TextStyle_500_12,
                                                        color = if (clickSkill) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(20.dp))

                                                Box(modifier = Modifier
                                                    .background(
                                                        if (clickVisit) ColorPrimary else Color.White,
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            ColorPrimary
                                                        ),
                                                        shape = RoundedCornerShape(7.dp)
                                                    )
                                                    .clickable {
                                                        clickDevp = false
                                                        clickBugFix = false
                                                        clickMeeting = false
                                                        clickModification = false
                                                        clickDemo = false
                                                        clickImpl = false
                                                        clickSkill = false
                                                        clickVisit = true
                                                        jobType.value = "VIST"
                                                    }
                                                    .width(82.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "VISIT",
                                                        style = TextStyle_400_12,
                                                        color = if (clickVisit) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(
                                                            vertical = 5.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }


                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Start Date", style = TextStyle_600_14,
                                            modifier = Modifier.width(100.dp),
                                            color = ColorPrimary
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            startDate = startDateCalender.value
                                            Box(
                                                modifier = Modifier
                                                    .border(
                                                        0.5.dp,
                                                        color = ColorPrimary,
                                                        shape = RoundedCornerShape(4.27.dp)
                                                    )
                                                    .width(100.dp)
                                                    .height(30.dp)
                                            ) {
                                                Text(
                                                    text = startDate.ifEmpty {
                                                        currentDateApi()
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

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Estimate Date ", style = TextStyle_600_14,
                                            modifier = Modifier.width(100.dp),
                                            color = ColorPrimary
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            estimateDate = estimateDateCalender.value
                                            Box(
                                                modifier = Modifier
                                                    .border(
                                                        0.5.dp,
                                                        color = ColorPrimary,
                                                        shape = RoundedCornerShape(4.27.dp)
                                                    )
                                                    .width(100.dp)
                                                    .height(30.dp)
                                            ) {
                                                Text(
                                                    text = estimateDate.ifEmpty {
                                                        currentDateApi()
                                                    },
                                                    style = TextStyle_400_14,
                                                    modifier = Modifier.padding(5.dp)
                                                )
                                            }

                                            Image(
                                                painter = painterResource(id = R.drawable.calender_icon),
                                                contentDescription = "calender icon",
                                                modifier = Modifier.clickable {
                                                    estimateDatePickerDialog.show()
                                                }
                                            )
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
                                                    BorderStroke(1.dp, ColorPrimary),
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
                                                    text = "Jira Id",
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
                                                    value = textJiraId.value,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Start)
                                                        .focusRequester(focusRequester),
                                                    onValueChange = {

                                                        textJiraId.value = it
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
                                                    BorderStroke(1.dp, ColorPrimary),
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
                                                    text = "Task Description",
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
                                                    value = textProjectDetails.value,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Start)
                                                        .focusRequester(focusRequester),
                                                    onValueChange = {

                                                        textProjectDetails.value = it
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
                                            if (textProjectDetails.value.isNotEmpty() && taskName.value.isNotEmpty()) {
                                                viewModel.addNewTask(
                                                    addNewTaskRequest = (
                                                        AddNewTaskRequest(
                                                            task_name = taskName.value,
                                                            task_no = state.taskCountData?.task_no!!,
                                                            task_jira_no = textJiraId.value,
                                                            project_name = selectedStatus,
                                                            task_details = textProjectDetails.value,
                                                            created_on = currentDateApi() +" "+ currentTime24(),
                                                            task_start_date = startDate,
                                                            task_estimate_date =estimateDate,
                                                            project_code = state.taskCountData?.project_code!!,
                                                            task_status = "TO DO"
                                                        )
                                                    ), onSuccess = {
                                                        textJiraId.value = ""
                                                        textProjectDetails.value = ""
                                                        taskName.value = ""
                                                        showSuccess = true
                                                    }
                                                )
                                            }
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    if (state.isLoading.not()) {
                                        Text(
                                            text = "Add Task",
                                            style = TextStyle_500_14,
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                top = dimensionResource(
                                                    id = R.dimen.dimen_3
                                                )
                                            )
                                        )
                                    }else {

                                        CircularProgressIndicator(
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

                }
            }
        }
    }

    if (showSuccess) {
        Messagebox(onSuccess = {
            showSuccess = false
            //onCancelClick.invoke()
        }, message = "New Task added!!")
    }

}