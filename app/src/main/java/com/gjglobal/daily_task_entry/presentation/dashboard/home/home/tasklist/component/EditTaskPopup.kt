package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdate.RecentUpdateItem
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_14
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import com.gjglobal.daily_task_entry.presentation.utils.formatTimeToHHmm
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTask(
    item: RecentUpdateItem,
    onClickCancelBtn: (() -> Unit),
) {
    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val pYear: Int
    val pMonth: Int
    val pDay: Int
    val calendar = Calendar.getInstance()
    pYear = calendar.get(Calendar.YEAR)
    pMonth = calendar.get(Calendar.MONTH)
    pDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val date = remember { mutableStateOf("") }
    var sYear: String
    var sMonth: String
    var sDay: String
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Select status") }
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf("Select level") }
    var expandedLevel by remember { mutableStateOf(false) }
    var textJobeDone by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    val listStatusItems =
        ArrayList(listOf("IN PROGRESS","COMPLETED", "IN HOLD"))
    var selectedDate: String = ""

    val listLevelItems =
        ArrayList(listOf("10","20","30","40","50","60","70","80","90","100"))

    startTime.value = item.start_time!!
    endTime.value = item.end_time!!
    date.value = item.date!!
    selectedLevel = item.completed_level!!
    selectedStatus = item.task_status!!
    textJobeDone = item.job_done!!

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


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 20.dp), // Adjust padding as needed
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp
    ) {
        Column( modifier = Modifier
            .background(LightBlue)
//                .background(
//                if (item.task_status == "COMPLETED") {
//                    doneColor
//                } else {
//                    inProgressColor
//                }
//                )
            .padding(5.dp)) {

            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()){
                Image(painter = painterResource(id = R.drawable.ic_round_cancel_24),
                    contentDescription = "roundCancel",
                    modifier = Modifier
                        .clickable {
                            onClickCancelBtn.invoke()
                        })
            }

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "Edit Task", style = TextStyle_800_14)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "TaskNo", style = TextStyle_500_12)
                Text(text = item.task_no ?: "No task number available",style = TextStyle_600_12, color = Color.Red) // Provide a default text if task_no is null
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "StartTime", style = TextStyle_500_12)
                Text(text = item.start_time ?: "No start time available",style = TextStyle_500_12)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "End Time", style = TextStyle_500_12)
                Text(text = item.end_time ?: "No end time available",style = TextStyle_500_12)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "JobDone", style = TextStyle_500_12)
                Text(text = item.job_done ?: "No job details available",style = TextStyle_500_12)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(text = "Status", style = TextStyle_500_12)
                Text(text = item.task_status ?: "No status available",style = TextStyle_500_12)
            }

//            Spacer(modifier = Modifier.height(15.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = " Date : ", style = TextStyle_400_14,
//                    modifier = Modifier.width(150.dp)
//                )
//                Row(
//                    modifier = Modifier.width(150.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    selectedDate = date.value
//                    Box(
//                        modifier = Modifier
//                            .background(Color.White)
//                            .border(
//                                0.5.dp,
//                                color = ColorPrimary,
//                                shape = RoundedCornerShape(4.27.dp)
//                            )
//                            .width(100.dp)
//                            .height(30.dp)
//                    ) {
//                        Text(text = formatDate(selectedDate), style = TextStyle_400_14, modifier = Modifier.padding(5.dp))
//                    }
//
//                    Image(
//                        painter = painterResource(id = R.drawable.calender_icon),
//                        contentDescription = "calender icon",
//                        modifier = Modifier.clickable {
//                            datePickerDialog.show()
//                        }
//                    )
//                }
//            }
            Spacer(modifier = Modifier.height(10.dp))
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
                            .background(Color.White)
                            .width(100.dp)
                            .height(30.dp)
                    ) {
                        Text(
                            text = formatTimeToHHmm(startTime.value),
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
                            .background(Color.White)
                            .width(100.dp)
                            .height(30.dp)
                    ) {
                        Text(
                            text = formatTimeToHHmm(endTime.value),
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


            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Select Status:",
                    style = TextStyle_400_14,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .background(color = Color.White)
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

            Spacer(modifier = Modifier.height(5.dp))

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
                            .background(color = Color.White)
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
                                    style = TextStyle_400_12,
                                    modifier = Modifier.background(Color.White)
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

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
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
                        style = TextStyle_400_12,
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
                        },
                        textStyle = TextStyle_400_14,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_5)))



            Spacer(modifier = Modifier.height(5.dp))

            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorPrimary),
                    modifier = Modifier
                        .width(80.dp)
                        .height(dimensionResource(id = R.dimen.dimen_40)),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_8)),
                    border = BorderStroke(
                        width = (dimensionResource(id = R.dimen.dimen_1)),
                        color = ColorPrimary
                    )
                ) {
                    Text(text = "Update", color = Color.White, style = TextStyle_600_12)

                }
            }
            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}
