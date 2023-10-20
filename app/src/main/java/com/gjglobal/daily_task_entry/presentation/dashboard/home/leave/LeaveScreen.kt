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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.leave.LeaveSaveRequest
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
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDate
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
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
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val state = viewModel.state.value

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
    var selectedDate: String = ""

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
                viewModel.getLeaveList(leaveListRequest = LeaveListRequest(staff_name = staffName!!))
                dashViewModel.hideBottomMenu(true)
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

            ToolBar(nameOfScreen = "Leave data", iconOfScreen = 0, onClick = {
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
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = " Date ", style = TextStyle_600_14,
                                                    modifier = Modifier.width(100.dp),
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
                                                                shape = RoundedCornerShape(4.27.dp)
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
                                                    .padding(horizontal = 20.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Leave Type",
                                                    style = TextStyle_600_14,
                                                    modifier = Modifier.width(100.dp),
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
                                                        dayType.value = "FullDay"

                                                    }
                                                    .width(100.dp),
                                                    contentAlignment = Alignment.Center) {
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
                                                        dayType.value = "HalfDay"
                                                        sessionType.value = "nil"
                                                    }
                                                    .width(82.dp),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "Half day",
                                                        style = TextStyle_400_12,
                                                        color = if (clickHalfDay) Color.White else ColorPrimary,
                                                        modifier = Modifier.padding(vertical = 5.dp)
                                                    )
                                                }


                                            }


                                            if (clickHalfDay) {
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
                                                                .padding(10.dp)
                                                        ) {
                                                            Text(
                                                                text = "Session",
                                                                style = TextStyle_600_14,
                                                                modifier = Modifier.width(100.dp),
                                                                color = ColorPrimary
                                                            )

                                                            Box(modifier = Modifier
                                                                .background(
                                                                    if (clickMorning) ColorPrimary else Color.White,
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
                                                                    sessionType.value = "Morning"
                                                                    clickMorning = true
                                                                    clickAfterNoon = false

                                                                }
                                                                .width(100.dp),
                                                                contentAlignment = Alignment.Center) {
                                                                Text(
                                                                    text = "Morning",
                                                                    style = TextStyle_500_12,
                                                                    color = if (clickMorning) Color.White else ColorPrimary,
                                                                    modifier = Modifier.padding(
                                                                        vertical = 5.dp
                                                                    )
                                                                )
                                                            }

                                                            Spacer(modifier = Modifier.width(20.dp))

                                                            Box(modifier = Modifier
                                                                .background(
                                                                    if (clickAfterNoon) ColorPrimary else Color.White,
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
                                                                    clickMorning = false
                                                                    clickAfterNoon = true
                                                                    sessionType.value = "AfterNoon"
                                                                }
                                                                .width(82.dp),
                                                                contentAlignment = Alignment.Center) {
                                                                Text(
                                                                    text = "After Noon",
                                                                    style = TextStyle_400_12,
                                                                    color = if (clickAfterNoon) Color.White else ColorPrimary,
                                                                    modifier = Modifier.padding(
                                                                        vertical = 5.dp
                                                                    )
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
                                                            text = "Leave reason",
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

                                                    if (textLeaveDetails.value.isNotEmpty() && dayType.value.isNotEmpty()) {
                                                        viewModel.saveLeave(
                                                            leaveSaveRequest = LeaveSaveRequest(
                                                                date = selectedDate,
                                                                staff_name = staffName.toString(),
                                                                leave_status = "1",
                                                                leave_details = textLeaveDetails.value,
                                                                created_on = currentDateApi() + " " + currentTime24(),
                                                                created_by = userData?.id!!,
                                                                day_type = dayType.value,
                                                                session_type = sessionType.value,
                                                                job_done = "Leave"
                                                            ), onSuccess = {
                                                                showSuccess = true
                                                                textLeaveDetails.value = ""
                                                                dayType.value = "FullDay"
                                                                sessionType.value = "nil"

                                                            }
                                                        )
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
                            
                            LeaveListCard(viewModel = viewModel)

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
        }, message = "Leave added!!")
    }

}

@Composable
fun LeaveListCard(viewModel: LeaveViewModel){

//        if (viewModel.state.value.isLoading) {
//            Box(modifier = Modifier.height(400.dp)) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//        }else{
            val leaveList1 by viewModel.leaveList.collectAsState()
            if (leaveList1.isNotEmpty()) {

                Box(contentAlignment = Alignment.Center,
                modifier = Modifier.padding()) {
                    Column() {
                        Row(horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)){
                            Text(text = "Date", style = TextStyle_600_14)
                            Spacer(modifier = Modifier.width(80.dp))
                            Text(text = "Type",style = TextStyle_600_14)
                            Spacer(modifier = Modifier.width(40.dp))
                            Text(text = "Reason",style = TextStyle_600_14)
                        }

                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxSize() // You can adjust the width/height of the divider using this modifier
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(modifier = Modifier.height(400.dp)) {
                            items(leaveList1) { item ->
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
                                        .height(50.dp)
                                        //.padding(horizontal = 20.dp, vertical = 2.dp)
//                                        .background(
//                                            Color.LightGray,
//                                            shape = RoundedCornerShape(5.dp)
//                                        )
//                                        .border(
//                                            BorderStroke(1.dp, Color.White),
//                                            shape = RoundedCornerShape(5.dp)
//                                        )
                                        ){
                                        Row(horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically){
                                            Text(modifier = Modifier.width(70.dp), text = formatDate(item.date),style = TextStyle_600_12,
                                                color = ColorPrimary)

                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(text = item.day_type,style = TextStyle_600_12,
                                                color = ColorPrimary)

                                            Spacer(modifier = Modifier.width(20.dp))
                                            Text(text = item.leave_details,style = TextStyle_600_12,
                                                color = ColorPrimary)
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
                    if(viewModel.state.value.isLoading.not()) {
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
                    }else{
                        Text(
                            text = "Loading...",
                            style = TextStyle_400_14,
                            modifier = Modifier.padding(top=100.dp)
                        )
                    }
                }

            }
        //}
}


