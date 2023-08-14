package com.gjglobal.daily_task_entry.presentation.dashboard.home.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
import com.gjglobal.daily_task_entry.presentation.dashboard.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    list: TaskListItem,
    viewModel: TaskListViewModel
) {
    val context = LocalContext.current
    val mContext = LocalContext.current
    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("09:30") }
    val endTime = remember { mutableStateOf("18:00") }
    var clickFullDay by remember { mutableStateOf(true) }
    var clickHalfDay by remember { mutableStateOf(false) }
    var clickCustom by remember { mutableStateOf(false) }
    var clickMorning by remember { mutableStateOf(false) }
    var clickAfterNoon by remember { mutableStateOf(false) }
    val dayType = remember { mutableStateOf("FullDay") }
    val sessionType = remember { mutableStateOf("nil") }
    val leaveStatus = remember { mutableStateOf("nil") }
    val leaveDetails = remember { mutableStateOf("nil") }
    val remarksAny = remember { mutableStateOf("nil") }
    val workAt = remember { mutableStateOf("Office") }
    val jiraNo = remember { mutableStateOf("nil") }
    var selectedDate: String
    var expandedStatus by remember { mutableStateOf(false) }
    var cardExpand by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select status") }
    var textJobeDone by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val listStatusItems =
        ArrayList(listOf("TODO", "IN PROGRESS", "CODE REVIEW", "COMPLETED", "IN HOLD"))


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

    var mmHour = ""
    var mmMinute = ""

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->

            mmHour = if (mHour.toString().length == 1) {
                "0$mHour"
            } else {
                mHour.toString()
            }

            mmMinute = if (mMinute.toString().length == 1) {
                "0$mMinute"
            } else {
                mMinute.toString()
            }
            if (mmHour.isEmpty()) {
                startTime.value = "09:30"
            } else {
                startTime.value = "$mmHour:$mmMinute"
            }

        }, mHour, mMinute, false
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->

            mmHour = if (mHour.toString().length == 1) {
                "0$mHour"
            } else {
                mHour.toString()
            }

            mmMinute = if (mMinute.toString().length == 1) {
                "0$mMinute"
            } else {
                mMinute.toString()
            }

            if (mmHour.toString().isEmpty()) {
                endTime.value = "18:00"
            } else {
                endTime.value = "$mmHour:$mmMinute"
            }
        }, mHour, mMinute, false
    )



    Column(Modifier.background(Color.White)) {

        Card(
            modifier = modifier
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
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.dimen_10),
                        vertical = dimensionResource(id = R.dimen.dimen_10)
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.notes),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.dimen_50))
                            .clickable {
                                cardExpand = !cardExpand
                            }

                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_20)))

                    Column {


                        Text(
                            text = list.project_name, style = TextStyle_500_16
                        )


                        Text(
                            text = list.task_name, style = TextStyle_400_12
                        )


                    }

                }
                Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.dimen_20))) {
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = "Assigned Date",
                            style = TextStyle_400_14
                        )


                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            textAlign = TextAlign.Start,
                            text = list.assigned_date,
                            style = TextStyle_400_14,
                        )

                    }
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = "Task Status",
                            style = TextStyle_400_14
                        )

                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = list.task_status,
                            style = TextStyle_400_14
                        )


                    }
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = "Task Details",
                            style = TextStyle_400_14
                        )
                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = list.task_details,
                            style = TextStyle_500_14
                        )

                    }

                    if (cardExpand) {
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = " Date : ", style = TextStyle_400_14,
                                modifier = Modifier.width(150.dp)
                            )
                            Row(
                                modifier = Modifier.width(150.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                selectedDate = date.value
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
                                    Text(text = selectedDate.ifEmpty {
                                        currentDateApi()
                                    }, style = TextStyle_400_14, modifier = Modifier.padding(5.dp))
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
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Selected Time : ${startTime.value}  ${endTime.value}",
                                style = TextStyle_400_14,
                            )

                            Text(
                                text = "Working Hrs..",
                                style = TextStyle_400_14,
                                color = ColorPrimary
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(modifier = Modifier
                                    .background(
                                        if (clickFullDay) ColorPrimary else Color.White,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .border(
                                        BorderStroke(1.dp, ColorPrimary),
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .clickable {
                                        clickFullDay = true
                                        clickHalfDay = false
                                        clickCustom = false
                                        clickMorning = false
                                        clickAfterNoon = false

                                    }
                                    .width(100.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Full day",
                                        style = TextStyle_500_12,
                                        color = if (clickFullDay) Color.White else ColorPrimary,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Box(modifier = Modifier
                                    .background(
                                        if (clickHalfDay) ColorPrimary else Color.White,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .border(
                                        BorderStroke(1.dp, ColorPrimary),
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .clickable {
                                        clickFullDay = false
                                        clickHalfDay = true
                                        clickCustom = false
                                        clickMorning = false
                                        clickAfterNoon = false
                                    }
                                    .width(82.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Half day",
                                        style = TextStyle_400_12,
                                        color = if (clickHalfDay) Color.White else ColorPrimary,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Box(modifier = Modifier
                                    .background(
                                        if (clickCustom) ColorPrimary else Color.White,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .border(
                                        BorderStroke(1.dp, ColorPrimary),
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .clickable {
                                        clickFullDay = false
                                        clickHalfDay = false
                                        clickCustom = true
                                        clickMorning = false
                                        clickAfterNoon = false
                                    }
                                    .width(82.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Custom",
                                        style = TextStyle_400_12,
                                        color = if (clickCustom) Color.White else ColorPrimary,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }
                            }

                            if (clickHalfDay) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Select Session",
                                        style = TextStyle_400_14,
                                        color = ColorPrimary
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(modifier = Modifier
                                            .background(
                                                if (clickMorning) ColorPrimary else Color.White,
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .border(
                                                BorderStroke(1.dp, ColorPrimary),
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .clickable {
                                                clickMorning = true
                                                clickAfterNoon = false

                                            }
                                            .width(100.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "Morning",
                                                style = TextStyle_500_12,
                                                color = if (clickMorning) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 3.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        Box(modifier = Modifier
                                            .background(
                                                if (clickAfterNoon) ColorPrimary else Color.White,
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .border(
                                                BorderStroke(1.dp, ColorPrimary),
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                            .clickable {
                                                clickMorning = false
                                                clickAfterNoon = true
                                            }
                                            .width(82.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "After Noon",
                                                style = TextStyle_400_12,
                                                color = if (clickAfterNoon) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (clickCustom) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Start Time : ", style = TextStyle_400_14,
                                        modifier = Modifier.width(150.dp)
                                    )
                                    Row(
                                        modifier = Modifier.width(150.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        //selectedFromDate = mTime.value
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
                                                text = startTime.value.ifEmpty {
                                                    currentTime()
                                                },
                                                style = TextStyle_400_14,
                                                modifier = Modifier.padding(5.dp)
                                            )
                                        }

                                        Image(
                                            painter = painterResource(id = R.drawable.calender_icon),
                                            contentDescription = "calender icon",
                                            modifier = Modifier.clickable {
                                                startTimePickerDialog.show()
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "End Time : ", style = TextStyle_400_14,
                                        modifier = Modifier.width(150.dp)
                                    )
                                    Row(
                                        modifier = Modifier.width(150.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        //selectedFromDate = mTime.value
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
                                                text = endTime.value.ifEmpty {
                                                    currentTime()
                                                },
                                                style = TextStyle_400_14,
                                                modifier = Modifier.padding(5.dp)
                                            )
                                        }

                                        Image(
                                            painter = painterResource(id = R.drawable.calender_icon),
                                            contentDescription = "calender icon",
                                            modifier = Modifier.clickable {
                                                endTimePickerDialog.show()
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))


                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Select Status:",
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
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_15)))
                            Box(
                                modifier = Modifier
                                    .width(311.dp)
                                    .background(LightBlue)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_5))
                                ) {
                                    Text(
                                        text = "Job done",
                                        color = TextColor,
                                        style = TextStyle_400_14,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_2)))
                                    BasicTextField(
                                        value = textJobeDone,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                            .focusRequester(focusRequester),
                                        onValueChange = {

                                            textJobeDone = it
                                            //                 viewModel.isValidUsername(it)
                                        },
                                        textStyle = TextStyle_500_16,
                                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
                                cardExpand = true
                                if (textJobeDone != "" && startTime.value.isNotEmpty()) {

                                    viewModel.saveTaskStatus(
                                        taskStatusRequest = TaskStatusRequest(
                                            assigned_date = list.assigned_date,
                                            created_by = list.staff_id,
                                            created_on = currentDateApi() + " " + currentTime24(),
                                            date = currentDateApi(),
                                            day_type = dayType.value,
                                            end_time = endTime.value,
                                            jira_no = list.task_jira_no,
                                            job_done = textJobeDone,
                                            leave_details = leaveDetails.value,
                                            leave_status = leaveStatus.value,
                                            project_name = list.project_name,
                                            project_status = list.project_status,
                                            remarks_any = remarksAny.value,
                                            session_type = sessionType.value,
                                            staff_name = list.staff_name,
                                            start_time = startTime.value,
                                            task_details = list.task_details,
                                            task_no = list.task_name,
                                            task_status = selectedStatus,
                                            work_at = workAt.value,
                                            id = list.id
                                        ), onSuccess = {
                                            onClick.invoke()
                                            cardExpand = false
                                        }
                                    )
                                }

                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Update Status",
                            style = TextStyle_500_14,
                            color = Color.White,
                            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_3))
                        )
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_20)))

    }

    if (clickFullDay) {
        startTime.value = "09:30"
        endTime.value = "18:00"
    }
    if (clickHalfDay) {
        startTime.value = ""
        endTime.value = ""
    }
    if (clickHalfDay && clickMorning) {
        startTime.value = "09:30"
        endTime.value = "13:00"
    }
    if (clickHalfDay && clickAfterNoon) {
        startTime.value = "14:30"
        endTime.value = "18:00"
    }
}

@Composable
fun MyContent() {
    // Fetching local context
    val mContext = LocalContext.current
    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("09:30") }
    val endTime = remember { mutableStateOf("18:00") }
    var clickFullDay by remember { mutableStateOf(true) }
    var clickHalfDay by remember { mutableStateOf(false) }
    var clickCustom by remember { mutableStateOf(false) }
    var clickMorning by remember { mutableStateOf(false) }
    var clickAfterNoon by remember { mutableStateOf(false) }


    var mmHour = ""
    var mmMinute = ""

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->

            mmHour = if (mHour.toString().length == 1) {
                "0$mHour"
            } else {
                mHour.toString()
            }

            mmMinute = if (mMinute.toString().length == 1) {
                "0$mMinute"
            } else {
                mMinute.toString()
            }
            if (mmHour.isEmpty()) {
                startTime.value = "09:30"
            } else {
                startTime.value = "$mmHour:$mmMinute"
            }

        }, mHour, mMinute, false
    )

    val endTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->

            mmHour = if (mHour.toString().length == 1) {
                "0$mHour"
            } else {
                mHour.toString()
            }

            mmMinute = if (mMinute.toString().length == 1) {
                "0$mMinute"
            } else {
                mMinute.toString()
            }

            if (mmHour.toString().isEmpty()) {
                endTime.value = "18:00"
            } else {
                endTime.value = "$mmHour:$mmMinute"
            }
        }, mHour, mMinute, false
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Time : ${startTime.value}  ${endTime.value}",
            style = TextStyle_400_14,
        )

        Text(
            text = "Working Hrs..",
            style = TextStyle_400_14,
            color = ColorPrimary
        )
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .background(
                    if (clickFullDay) ColorPrimary else Color.White,
                    shape = RoundedCornerShape(7.dp)
                )
                .border(
                    BorderStroke(1.dp, ColorPrimary),
                    shape = RoundedCornerShape(7.dp)
                )
                .clickable {
                    clickFullDay = true
                    clickHalfDay = false
                    clickCustom = false
                    clickMorning = false
                    clickAfterNoon = false

                }
                .width(100.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Full day",
                    style = TextStyle_500_12,
                    color = if (clickFullDay) Color.White else ColorPrimary,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Box(modifier = Modifier
                .background(
                    if (clickHalfDay) ColorPrimary else Color.White,
                    shape = RoundedCornerShape(7.dp)
                )
                .border(
                    BorderStroke(1.dp, ColorPrimary),
                    shape = RoundedCornerShape(7.dp)
                )
                .clickable {
                    clickFullDay = false
                    clickHalfDay = true
                    clickCustom = false
                    clickMorning = false
                    clickAfterNoon = false
                }
                .width(82.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Half day",
                    style = TextStyle_400_12,
                    color = if (clickHalfDay) Color.White else ColorPrimary,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Box(modifier = Modifier
                .background(
                    if (clickCustom) ColorPrimary else Color.White,
                    shape = RoundedCornerShape(7.dp)
                )
                .border(
                    BorderStroke(1.dp, ColorPrimary),
                    shape = RoundedCornerShape(7.dp)
                )
                .clickable {
                    clickFullDay = false
                    clickHalfDay = false
                    clickCustom = true
                    clickMorning = false
                    clickAfterNoon = false
                }
                .width(82.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Custom",
                    style = TextStyle_400_12,
                    color = if (clickCustom) Color.White else ColorPrimary,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

    }

    if (clickHalfDay) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Session",
                style = TextStyle_400_14,
                color = ColorPrimary
            )
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .background(
                        if (clickMorning) ColorPrimary else Color.White,
                        shape = RoundedCornerShape(7.dp)
                    )
                    .border(
                        BorderStroke(1.dp, ColorPrimary),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .clickable {
                        clickMorning = true
                        clickAfterNoon = false

                    }
                    .width(100.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Morning",
                        style = TextStyle_500_12,
                        color = if (clickMorning) Color.White else ColorPrimary,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Box(modifier = Modifier
                    .background(
                        if (clickAfterNoon) ColorPrimary else Color.White,
                        shape = RoundedCornerShape(7.dp)
                    )
                    .border(
                        BorderStroke(1.dp, ColorPrimary),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .clickable {
                        clickMorning = false
                        clickAfterNoon = true
                    }
                    .width(82.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "After Noon",
                        style = TextStyle_400_12,
                        color = if (clickAfterNoon) Color.White else ColorPrimary,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }

    if (clickCustom) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Start Time : ", style = TextStyle_400_14,
                modifier = Modifier.width(150.dp)
            )
            Row(
                modifier = Modifier.width(150.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                //selectedFromDate = mTime.value
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
                    Text(text = startTime.value.ifEmpty {
                        currentTime()
                    }, style = TextStyle_400_14, modifier = Modifier.padding(5.dp))
                }

                Image(
                    painter = painterResource(id = R.drawable.calender_icon),
                    contentDescription = "calender icon",
                    modifier = Modifier.clickable {
                        startTimePickerDialog.show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "End Time : ", style = TextStyle_400_14,
                modifier = Modifier.width(150.dp)
            )
            Row(
                modifier = Modifier.width(150.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                //selectedFromDate = mTime.value
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
                    Text(text = endTime.value.ifEmpty {
                        currentTime()
                    }, style = TextStyle_400_14, modifier = Modifier.padding(5.dp))
                }

                Image(
                    painter = painterResource(id = R.drawable.calender_icon),
                    contentDescription = "calender icon",
                    modifier = Modifier.clickable {
                        endTimePickerDialog.show()
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))


    if (clickFullDay) {
        startTime.value = "09:30"
        endTime.value = "18:00"
    }
    if (clickHalfDay) {
        startTime.value = ""
        endTime.value = ""
    }
    if (clickHalfDay && clickMorning) {
        startTime.value = "09:30"
        endTime.value = "13:00"
    }
    if (clickHalfDay && clickAfterNoon) {
        startTime.value = "14:30"
        endTime.value = "18:00"
    }

}