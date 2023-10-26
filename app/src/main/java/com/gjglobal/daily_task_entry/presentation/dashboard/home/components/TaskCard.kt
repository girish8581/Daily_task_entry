package com.gjglobal.daily_task_entry.presentation.dashboard.home.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskUpdateRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskListItem
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskStatusRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.qatask.QaTaskRequest
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDate
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24HHMM
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    list: TaskListItem,
    viewModel: TaskListViewModel,
    buttonEnable:Boolean,
    taskStatus:String,
    onStatusUpdate: () -> Unit,
    qaEnable:Boolean
) {
    val context = LocalContext.current
    val mContext = LocalContext.current
    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val clickFullDay by remember { mutableStateOf(false) }
    val clickHalfDay by remember { mutableStateOf(false) }
    val clickCustom by remember { mutableStateOf(true) }
    val clickMorning by remember { mutableStateOf(false) }
    val clickAfterNoon by remember { mutableStateOf(false) }
    val dayType = remember { mutableStateOf("Hourly") }
    val sessionType = remember { mutableStateOf("nil") }
    val leaveStatus = remember { mutableStateOf(0) }
    val leaveDetails = remember { mutableStateOf("nil") }
    val remarksAny = remember { mutableStateOf("nil") }
    val workAt = remember { mutableStateOf("Office") }
    var selectedDate: String = ""

    var expandedLevel by remember { mutableStateOf(false) }
    var cardExpand by remember { mutableStateOf(false) }
    var qaCardExpand by remember { mutableStateOf(false) }
    var clickCount by remember { mutableStateOf(0) }
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select status") }
    var selectedLevel by remember { mutableStateOf("Select level") }
    var textJobeDone by remember { mutableStateOf("") }
    var selectedBreakHours by remember { mutableStateOf("Select") }
    var expandedBreakHours by remember { mutableStateOf(false) }

    var selectedQAStatus by remember { mutableStateOf("Select status") }
    var expandedQAStatus by remember { mutableStateOf(false) }
    var isMoveInProgress by remember { mutableStateOf(false) }


    val listQAStatusItems =
        ArrayList(listOf("Ready for QA Testing","Refinement not ready","QA Testing Progress", "QA Testing Passed","QA Testing Failed"))

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val listStatusItems =
        ArrayList(listOf("IN PROGRESS","In QA Testing","TO DO"))

    val listLevelItems =
        ArrayList(listOf("10","20","30","40","50","60","70","80","90","100"))

    val listBreakHours =
        ArrayList(listOf("0","0.25","0.5","1","1.5","2","2.5","3"))

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
            elevation = 5.dp
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
                        painter = painterResource(id = when (taskStatus) {
                            "COMPLETED" -> {R.drawable.done_icon}
                            "TO DO" -> { R.drawable.todo_icon}
                            "IN PROGRESS" -> { R.drawable.inprogress}
                            "In QA Testing" -> {R.drawable.qa_icon}
                            else -> { R.drawable.notes}
                        }
                        ),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.dimen_50))
                            .clickable {
                                if(taskStatus=="IN PROGRESS"){
                                    cardExpand = !cardExpand
                                }
                                if(taskStatus == "In QA Testing"){
                                    qaCardExpand = !qaCardExpand
                                }

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


                    if(taskStatus=="In QA Testing"){
                        //qa task status and number
                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = "QA Task No",
                                style = TextStyle_400_14,

                            )

                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = list.qa_task_no,
                                style = TextStyle_400_14,
                                color = Color.Red
                            )


                        }

                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = "QA Task Status",
                                style = TextStyle_400_14
                            )

                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = list.qa_task_status,
                                style = TextStyle_400_14
                            )


                        }
                    }
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            text = if(taskStatus=="In QA Testing"){"Created Date" }else{"Assigned Date"},
                            style = TextStyle_400_14
                        )

                        Text(
                            modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                            textAlign = TextAlign.Start,
                            text = if(taskStatus=="In QA Testing"){list.qa_created_on }else{formatDate(list.assigned_date)},
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

                    // not required for QA Card
                    if(!qaEnable) {
                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = "Allotted time",
                                style = TextStyle_400_14
                            )
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = list.taskTime + " Hours",
                                style = TextStyle_600_14,
                                color = ColorPrimary
                            )

                        }

                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = "Total Time Taken",
                                style = TextStyle_400_14
                            )
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = list.time_taken + " Hours",
                                style = TextStyle_600_14,
                                color = if ((list.time_taken?.toDouble()
                                        ?: 0.00) > (list.taskTime?.toDouble() ?: 0.00)
                                ) {
                                    Color.Red
                                } else {
                                    DarkGreenColor
                                }

                            )

                        }

                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = "Completed level",
                                style = TextStyle_400_14
                            )
                            Text(
                                modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                text = list.completed_level + " %",
                                style = TextStyle_500_14,
                                color = if ((list.time_taken?.toDouble()
                                        ?: 0.00) > (list.taskTime?.toDouble() ?: 0.00)
                                ) {
                                    Color.Red
                                } else {
                                    DarkGreenColor
                                }

                            )

                        }

                        if(list.qa_task_status != "NA") {
                            Row(
                                modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                    text = "Remarks",
                                    style = TextStyle_400_14
                                )
                                Text(
                                    modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                    text = list.qa_task_status,
                                    style = TextStyle_500_14,
                                    color = if (list.qa_task_status != "NA"
                                    ) {
                                        if (taskStatus == "COMPLETED") {
                                            DarkGreenColor
                                        } else {
                                            Color.Red
                                        }
                                    } else {
                                        ColorPrimary
                                    }
                                )
                            }

                            Row(
                                modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                    text = "Reference",
                                    style = TextStyle_400_14
                                )
                                Text(
                                    modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                    text = list.qa_task_no,
                                    style = TextStyle_500_14,
                                    color = if (list.qa_task_status != "NA"
                                    ) {
                                        if (taskStatus == "COMPLETED") {
                                            DarkGreenColor
                                        } else {
                                            Color.Red
                                        }
                                    } else {
                                        ColorPrimary
                                    }
                                )
                            }

                            if (list.qa_created_on.isNullOrBlank().not()){
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_150)),
                                        text = "QA updated on ${list.qa_created_on}",
                                        style = TextStyle_400_12,
                                        color = ColorPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                        }

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
                                    Text(text = formatDate(selectedDate).ifEmpty {
                                        currentDate()
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
                        Spacer(modifier = Modifier.height(15.dp))
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

                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // TO DO Status not required time slot selection//
                            if(selectedStatus!="TO DO") {

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
                                                        currentTime24HHMM()
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
                                            verticalAlignment = Alignment.CenterVertically
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
                                                        currentTime24HHMM()
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
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Break hours",
                                        style = TextStyle_400_14,
                                        modifier = Modifier.width(150.dp),
                                        color = Color.Red

                                    )

                                    Spacer(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                0.5.dp,
                                                color = ColorPrimary,
                                                shape = RoundedCornerShape(4.27.dp)
                                            )
                                            .width(80.dp)
                                            .height(35.dp)
                                    ) {
                                        ExposedDropdownMenuBox(
                                            expanded = expandedBreakHours,
                                            onExpandedChange = {
                                                expandedBreakHours = !expandedBreakHours
                                            }) {
                                            ExposedDropdownMenu(expanded = expandedBreakHours,
                                                onDismissRequest = { expandedBreakHours = false }) {
                                                listBreakHours.forEach { selectedOption ->
                                                    DropdownMenuItem(onClick = {
                                                        selectedBreakHours = selectedOption
                                                        expandedBreakHours = false
                                                    }) {
                                                        Text(
                                                            text = selectedOption,
                                                            style = TextStyle_400_14,
                                                            fontWeight = if (selectedOption == selectedBreakHours) FontWeight.Bold else null
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
                                                        expandedBreakHours = true
                                                    },

                                                ) {
                                                Text(
                                                    text = selectedBreakHours,
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
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Hrs",
                                        style = TextStyle_400_14,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if(selectedStatus=="In QA Testing") {
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Select QA Status:",
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
                                            expanded = expandedQAStatus,
                                            onExpandedChange = {
                                                expandedQAStatus = !expandedQAStatus
                                            }) {
                                            ExposedDropdownMenu(expanded = expandedQAStatus,
                                                onDismissRequest = { expandedQAStatus = false }) {
                                                listQAStatusItems.forEach { selectedOption ->
                                                    DropdownMenuItem(onClick = {
                                                        selectedQAStatus = selectedOption
                                                        expandedQAStatus = false
                                                    }) {
                                                        Text(
                                                            text = selectedOption,
                                                            style = TextStyle_400_12,
                                                            fontWeight = if (selectedOption == selectedQAStatus) FontWeight.Bold else null
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
                                                        expandedQAStatus = true
                                                    },

                                                ) {
                                                Text(
                                                    text = selectedQAStatus,
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
                            }

                            if(selectedStatus=="IN PROGRESS") {

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Select completed level in %",
                                        style = TextStyle_400_14,
                                        modifier = Modifier.width(200.dp)
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
                                            expanded = expandedLevel,
                                            onExpandedChange = {
                                                expandedLevel = !expandedLevel
                                            }) {
                                            ExposedDropdownMenu(expanded = expandedLevel,
                                                onDismissRequest = { expandedLevel = false }) {
                                                listLevelItems.forEach { selectedOption ->
                                                    DropdownMenuItem(onClick = {
                                                        selectedLevel = selectedOption
                                                        expandedLevel = false
                                                    }) {
                                                        Text(
                                                            text = selectedOption,
                                                            style = TextStyle_400_12,
                                                            fontWeight = if (selectedOption == selectedLevel) FontWeight.Bold else null
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
                                                        expandedLevel = true
                                                    },

                                                ) {
                                                Text(
                                                    text = selectedLevel,
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
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_10)))
                            Box(
                                modifier = Modifier
                                    .width(311.dp)
                                    //.background(LightBlue)
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
                    if(qaCardExpand){
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
                                    Text(text = formatDate(selectedDate).ifEmpty {
                                        currentDate()
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
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Select QA Status:",
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
                                        expanded = expandedQAStatus,
                                        onExpandedChange = {
                                            expandedQAStatus = !expandedQAStatus
                                        }) {
                                        ExposedDropdownMenu(expanded = expandedQAStatus,
                                            onDismissRequest = { expandedQAStatus = false }) {
                                            listQAStatusItems.forEach { selectedOption ->
                                                DropdownMenuItem(onClick = {
                                                    selectedQAStatus = selectedOption
                                                    expandedQAStatus = false
                                                }) {
                                                    Text(
                                                        text = selectedOption,
                                                        style = TextStyle_400_12,
                                                        fontWeight = if (selectedOption == selectedQAStatus) FontWeight.Bold else null
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
                                                    expandedQAStatus = true
                                                },

                                            ) {
                                            Text(
                                                text = selectedQAStatus,
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

                            if(selectedQAStatus=="QA Testing Progress" || (selectedQAStatus=="QA Testing Failed")) {

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Select completed level in %",
                                        style = TextStyle_400_14,
                                        modifier = Modifier.width(200.dp)
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
                                            expanded = expandedLevel,
                                            onExpandedChange = {
                                                expandedLevel = !expandedLevel
                                            }) {
                                            ExposedDropdownMenu(expanded = expandedLevel,
                                                onDismissRequest = { expandedLevel = false }) {
                                                listLevelItems.forEach { selectedOption ->
                                                    DropdownMenuItem(onClick = {
                                                        selectedLevel = selectedOption
                                                        expandedLevel = false
                                                    }) {
                                                        Text(
                                                            text = selectedOption,
                                                            style = TextStyle_400_12,
                                                            fontWeight = if (selectedOption == selectedLevel) FontWeight.Bold else null
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
                                                        expandedLevel = true
                                                    },

                                                ) {
                                                Text(
                                                    text = selectedLevel,
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
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_10)))
                            Box(
                                modifier = Modifier
                                    .width(311.dp)
                                    //.background(LightBlue)
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
                        }
                    }

                }

                if(taskStatus == "TO DO"){
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

                                    isMoveInProgress = true
                                }, contentAlignment = Alignment.Center
                        ) {

                            if (viewModel.state.value.isLoading.not()) {
                                Text(
                                    text = "Move to In Progress",
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

                if(qaEnable){
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
                                    qaCardExpand = true
                                    clickCount++
                                    if (textJobeDone != ""
                                        && selectedQAStatus != ""
                                        && selectedLevel != "Select level"
                                    ) {
                                        viewModel.saveQaTaskStatus(
                                            QaTaskRequest(
                                                createdBy = list.staff_id,
                                                createdOn = currentDateApi() + " " + currentTime24(),
                                                date = selectedDate,
                                                jiraNo = list.task_jira_no,
                                                jobDone = textJobeDone,
                                                projectName = list.project_name,
                                                staffName = list.staff_name,
                                                taskDetails = list.task_details,
                                                taskNo = list.task_name,
                                                taskStatus ="In QA Testing",
                                                id = list.id,
                                                completedLevel = selectedLevel,
                                                qaTaskStatus = selectedQAStatus,
                                                qaTaskNo = list.qa_task_no
                                        ), onSuccess = {
                                                clickCount = 0
                                                viewModel.state.value.taskList = null
                                                onStatusUpdate.invoke()
                                                qaCardExpand = false
                                            }
                                        )

                                    } else {
                                        println("card-expand $qaCardExpand")

                                        if (clickCount != 1) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Please fill all data correctly!!",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }

                                    }

                                }, contentAlignment = Alignment.Center
                        ) {

                            if (viewModel.state.value.isLoading.not()) {
                                Text(
                                    text = "Update",
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

                if (buttonEnable){
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
                                    clickCount++
                                    if (textJobeDone != ""
                                        && startTime.value.isNotEmpty()
                                        && endTime.value.isNotEmpty()
                                        && selectedLevel.isNotEmpty()
                                        && selectedStatus != "Select status"
                                        && selectedBreakHours != "Select"
                                        && startTime.value != endTime.value
                                    ) {

                                        viewModel.saveTaskStatus(
                                            taskStatusRequest = TaskStatusRequest(
                                                assigned_date = list.assigned_date,
                                                created_by = list.staff_id,
                                                created_on = currentDateApi() + " " + currentTime24(),
                                                date = selectedDate.toString(),
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
                                                id = list.id,
                                                completed_level = selectedLevel,
                                                break_hours = (selectedBreakHours.toDouble() * 60).toString()
                                            ), onSuccess = {
                                                clickCount = 0
                                                viewModel.state.value.taskList = null
                                                onClick.invoke()
                                                cardExpand = false
                                            }
                                        )
                                    } else {
                                        println("card-expand $cardExpand")

                                        if (clickCount != 1) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Please fill all data correctly!!",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }

                                    }

                                }, contentAlignment = Alignment.Center
                        ) {

                            if (viewModel.state.value.isLoading.not()) {
                                Text(
                                    text = "Update",
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

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_20)))

    }
    
    if(isMoveInProgress){
        ConfirmAlertDialog(showDialog =remember { mutableStateOf(isMoveInProgress) } ,
            onConfirm = {
                isMoveInProgress = false
                viewModel.updateTaskStatus(
                    TaskUpdateRequest(
                        task_status = "IN PROGRESS",
                        updated_date = currentDateApi(),
                        entry_date = currentDateApi(),
                        task_name = list.task_name
                    ), id = list.id, onSuccess = {
                        onStatusUpdate.invoke()
                    }
                ) }, onCancel = {
                isMoveInProgress = false
            })
        
    }

    if(selectedQAStatus== "Ready for QA Testing" || selectedQAStatus == "Refinement not ready" ){
        selectedLevel = "0"
    }

    if(selectedQAStatus== "QA Testing Passed"){
        selectedLevel = "100"
    }


    if(selectedStatus=="In QA Testing"){
        selectedLevel = "100"
        selectedQAStatus = "Ready for QA Testing"
    }

    if(selectedStatus=="IN PROGRESS"){
        selectedLevel = ""
        selectedQAStatus = "Development in progress"
    }

    if(selectedStatus=="TO DO"){
        selectedLevel = "0"
        selectedQAStatus = "Development not started"
        startTime.value = "09:30"
        endTime.value = "09:35"
        dayType.value = "-NA-"
        selectedBreakHours = "0"
    }

    if (clickFullDay) {
        startTime.value = "09:30"
        endTime.value = "18:00"
        dayType.value = "FullDay"
    }
    if (clickHalfDay) {
        startTime.value = ""
        endTime.value = ""
        dayType.value = "HalfDay"
    }
    if (clickHalfDay && clickMorning) {
        startTime.value = "09:30"
        endTime.value = "13:00"
        dayType.value = "HalfDay"
        sessionType.value = "Morning"

    }
    if (clickHalfDay && clickAfterNoon) {
        startTime.value = "14:30"
        endTime.value = "18:00"
        dayType.value = "HalfDay"
        sessionType.value = "Afternoon"
    }
    if (clickCustom) {
        dayType.value = "Hourly"
    }
}

@Composable
fun ConfirmAlertDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismiss request (e.g., pressing outside the dialog)
                onCancel()
                showDialog.value = false
            },
            title = {
                Text(text = "Confirm Move Task",style = TextStyle_600_14, color = ColorPrimary)
            },
            text = {
                Text(text = "Are you sure you want to proceed?",style = TextStyle_500_12, color = ColorPrimary)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        showDialog.value = false
                    }
                ) {
                    Text("Confirm",style = TextStyle_500_12, color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancel()
                        showDialog.value = false
                    }
                ) {
                    Text("Cancel",style = TextStyle_500_12, color = ColorPrimary)
                }
            }
        )
    }
}

