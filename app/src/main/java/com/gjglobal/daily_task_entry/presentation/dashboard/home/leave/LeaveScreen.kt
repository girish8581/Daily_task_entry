package com.gjglobal.daily_task_entry.presentation.dashboard.home.leave

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.domain.model.tasklist.TaskStatusRequest
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
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import java.util.Calendar
import java.util.Date

@Composable
fun LeaveScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: LeaveViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var staffName: String? = null
    staffName = "Girish Kumar.G"
    val dayType = remember { mutableStateOf("FullDay") }
    val sessionType = remember { mutableStateOf("nil") }
    val leaveStatus = remember { mutableStateOf("nil") }
    val leaveDetails = remember { mutableStateOf("nil") }
    var clickFullDay by remember { mutableStateOf(true) }
    var clickHalfDay by remember { mutableStateOf(false) }
    var clickMorning by remember { mutableStateOf(false) }
    var clickAfterNoon by remember { mutableStateOf(false) }
    val textLeaveDetails = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var showSuccess by remember { mutableStateOf(false) }
    var selectedDate: String

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

                dashViewModel.hideBottomMenu(false)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            ToolBar(nameOfScreen = "Task list", iconOfScreen = 0, onClick = {
                navController.popBackStack()
            }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))

            if (viewModel.state.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
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

                                    Spacer(modifier = Modifier.height(15.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(horizontal = 20.dp),
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

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Leave Type",
                                            style = TextStyle_600_14,
                                            color = ColorPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Leave Type",
                                            style = TextStyle_600_14,
                                            color = ColorPrimary
                                        )

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
                                                clickMorning = false
                                                clickAfterNoon = false

                                            }
                                            .width(100.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "Full day",
                                                style = TextStyle_500_12,
                                                color = if (clickFullDay) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 5.dp)
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
                                                clickMorning = false
                                                clickAfterNoon = false
                                            }
                                            .width(82.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "Half day",
                                                style = TextStyle_400_12,
                                                color = if (clickHalfDay) Color.White else ColorPrimary,
                                                modifier = Modifier.padding(vertical = 5.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }

                                    if (clickHalfDay) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Select Session",
                                                    style = TextStyle_400_14,
                                                    color = ColorPrimary
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
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
                                                        .width(100.dp),
                                                        contentAlignment = Alignment.Center) {
                                                        Text(
                                                            text = "Morning",
                                                            style = TextStyle_500_12,
                                                            color = if (clickMorning) Color.White else ColorPrimary,
                                                            modifier = Modifier.padding(vertical = 5.dp)
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
                                                        .width(82.dp),
                                                        contentAlignment = Alignment.Center) {
                                                        Text(
                                                            text = "After Noon",
                                                            style = TextStyle_400_12,
                                                            color = if (clickAfterNoon) Color.White else ColorPrimary,
                                                            modifier = Modifier.padding(vertical = 5.dp)
                                                        )
                                                    }
                                                }


                                            }
                                            Spacer(modifier = Modifier.height(10.dp))

                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .background(LightBlue)
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_5))
                                            ) {
                                                Text(
                                                    text = "Leave details",
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
                                                    value = textLeaveDetails.value,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Start)
                                                        .focusRequester(focusRequester),
                                                    onValueChange = {

                                                        textLeaveDetails.value = it
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
                                        .background(todoColor)
                                        .fillMaxWidth()
                                        .height(dimensionResource(id = R.dimen.dimen_50))
                                        .clickable {
                                                   showSuccess = true

                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Add leave status",
                                        style = TextStyle_500_14,
                                        color = Color.White,
                                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_3))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSuccess) {
        Messagebox(onSuccess = {
            showSuccess = false
            //onCancelClick.invoke()
        }, message = "Status updated!!")
    }
}

