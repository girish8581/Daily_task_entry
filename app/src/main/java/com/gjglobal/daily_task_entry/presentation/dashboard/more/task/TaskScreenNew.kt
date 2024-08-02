package com.gjglobal.daily_task_entry.presentation.dashboard.more.task

import ShimmerEffectListView
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginData
import com.gjglobal.daily_task_entry.domain.domain.model.task.AddNewTaskRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMappingRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskdata.newtask.NewTaskItem
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.sendEmail
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
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentTime24
import com.gjglobal.daily_task_entry.presentation.utils.formatDate
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskViewModel = hiltViewModel(),

    ) {
    val taskAssignViewModel :TaskAssignViewModel =  hiltViewModel()
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val state = viewModel.state.value
    val taskAssignState = taskAssignViewModel.state.value
    var addTaskVisible by remember { mutableStateOf(false) }
    val showConfirmation by remember { mutableStateOf(false) }


    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.getProjects()
                taskAssignViewModel.getStaffs()

                if(userData!!.userType =="USER"){
                    viewModel.getNewTaskList(staffName = userData.staff_name!!)
                }else{
                    viewModel.getNewTaskList(staffName = "ALL")
                }
                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }

    Scaffold(
        topBar = {
            ToolBar(nameOfScreen = "Add Task", iconOfScreen = 0, onClick = {
                if (addTaskVisible){
                    addTaskVisible = false
                }else {
                    navController.popBackStack()
                }
            }, onIconClick = {

            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addTaskVisible = true},
                backgroundColor = ColorPrimary,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.plus_icon),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        },
       content = {

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

           val listStatusItems =ArrayList<String>()
           state.projectList?.forEach {
               listStatusItems.add(it.project_name)
           }

           Box(
               modifier = Modifier.fillMaxSize()
           ) {
               if(addTaskVisible) {
                   Column(modifier = Modifier.fillMaxSize(),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Center) {
                       Box(modifier = Modifier.fillMaxSize()) {
                           Column {
                               //NewlyAddedTasks(viewModel=viewModel)
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
                                                   androidx.compose.material.Text(
                                                       text = "Select Project",
                                                       style = TextStyle_600_14,
                                                       color = ColorPrimary,
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
                                                               onDismissRequest = {
                                                                   expandedStatus = false
                                                               }) {
                                                               listStatusItems.forEach { selectedOption ->
                                                                   DropdownMenuItem(onClick = {
                                                                       selectedStatus = selectedOption
                                                                       state.isTaskCountData = false
                                                                       viewModel.getTaskCount(
                                                                           selectedStatus
                                                                       )
                                                                       expandedStatus = false
                                                                   }) {
                                                                       androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                   androidx.compose.material.Text(
                                                       text = "Task No",
                                                       style = TextStyle_600_14,
                                                       color = ColorPrimary,
                                                       modifier = Modifier.width(100.dp)
                                                   )
                                                   Spacer(modifier = Modifier.width(20.dp))

                                                   if (state.isTaskCountLoding!!.not()) {
                                                       if (state.isTaskCountData!!) {
                                                           taskName.value =
                                                               state.taskCountData?.project_code!! + "-" + jobType.value + "-" + state.taskCountData?.task_no!!
                                                           androidx.compose.material.Text(
                                                               text = state.taskCountData?.project_code!! + "-" + jobType.value + "-" + state.taskCountData?.task_no!!,
                                                               style = TextStyle_600_14,
                                                               color = Color.Red
                                                           )
                                                       }
                                                   } else {
                                                       AnimatedVisibility(visible = state.isTaskCountLoding!!) {
                                                           CircularProgressIndicator(
                                                               color = ColorPrimary,
                                                               modifier = Modifier
                                                                   .size(20.dp),
                                                               strokeWidth = 2.dp
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
                                                   androidx.compose.material.Text(
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
                                                       androidx.compose.material.Text(
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
                                                       androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                               androidx.compose.material.Text(
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
                                                   androidx.compose.material.Text(
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
                                                           androidx.compose.material.Text(
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
                                                   androidx.compose.material.Text(
                                                       text = "Estimate Date ",
                                                       style = TextStyle_600_14,
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
                                                           androidx.compose.material.Text(
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
                                                           androidx.compose.material.Text(
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
                                                           androidx.compose.material.Text(
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
                                                                           task_jira_no = "nil",
                                                                           project_name = selectedStatus,
                                                                           task_details = textProjectDetails.value,
                                                                           created_on = currentDateApi() + " " + currentTime24(),
                                                                           task_start_date = startDate,
                                                                           task_estimate_date = estimateDate,
                                                                           project_code = state.taskCountData?.project_code!!,
                                                                           task_status = "TO DO",
                                                                           created_by = userData?.staff_name!!
                                                                       )
                                                                       ), onSuccess = {

                                                                   showSuccess = true
                                                                   viewModel.getTaskCount(selectedStatus)
                                                                   viewModel.getNewTaskList(staffName = userData.staff_name)
                                                                   // update task no
                                                                   var mailBody = ""
                                                                   mailBody = "Hello Admin,\n\nNew Task (${taskName.value}) has been created by $staffName in the project $selectedStatus." +
                                                                           "please give approval"+
                                                                           "\n Jira Id :${textJiraId.value}"+
                                                                           "\n\n\n With Regards"+
                                                                           "\n TaskEntryApp"

                                                                   println(mailBody)
                                                                   sendEmail(
                                                                       subject = "Reg:Task approval request from $staffName - ${taskName.value}",
                                                                       content = mailBody,
                                                                       context = context,
                                                                       toMail = "girish.g@gjglobalsoft.com",
                                                                       onSuccess = {

                                                                       }
                                                                   )

                                                                   textJiraId.value = ""
                                                                   textProjectDetails.value = ""
                                                                   taskName.value = ""

                                                               }
                                                           )


                                                       }
                                                   }, contentAlignment = Alignment.Center
                                           ) {
                                               if (state.isLoading.not()) {
                                                   androidx.compose.material.Text(
                                                       text = "Add Task",
                                                       style = TextStyle_500_14,
                                                       color = Color.White,
                                                       modifier = Modifier.padding(
                                                           top = dimensionResource(
                                                               id = R.dimen.dimen_3
                                                           )
                                                       )
                                                   )
                                               } else {

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
               }else{
                   if(state.isGetNewTask!!) {
                       LazyColumnExample(state.newTaskList!!, taskAssignViewModel = taskAssignViewModel,
                           taskViewModel = viewModel,
                           userData = userData!!, context = context)
                   }else{
                       if(state.isLoading){
                           Row(modifier = Modifier.fillMaxWidth(),
                               horizontalArrangement = Arrangement.Center,
                               verticalAlignment = Alignment.CenterVertically){

                               ShimmerEffectListView(state.isLoading)
                               //CircularProgressIndicator(modifier = Modifier.padding(vertical = 20.dp))
                           }

                       }
                   }
               }

           }

           if(showConfirmation){

           }

           if (showSuccess) {
               Messagebox(onSuccess = {
                   showSuccess = false
                   //onCancelClick.invoke()
               }, message = "New Task added!!")
           }
        }
    )

    @Composable
    fun addTaskContent(){


    }
}

@Composable
fun LazyColumnExample(data:List<NewTaskItem>,taskAssignViewModel: TaskAssignViewModel,taskViewModel:TaskViewModel,userData: LoginData,context: Context) {
    var showDialog by remember { mutableStateOf(false)}
    var selectedItem by remember { mutableStateOf<NewTaskItem?>(null) }

    LazyColumn {
        items(data) { item ->
            ItemRow(item) {
                selectedItem = item
                showDialog = true
            }
        }
    }

    if(showDialog){
        ShowConfirmationDialog(onConfirm = {
            showDialog = false
        }, taskAssignViewModel = taskAssignViewModel, viewModel =taskViewModel, item = selectedItem!!,userData = userData , context =context )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowConfirmationDialog(onConfirm: () -> Unit,taskAssignViewModel:TaskAssignViewModel,
                           viewModel:TaskViewModel,
                           item:NewTaskItem,userData:LoginData,context:Context) {
    // Use AlertDialog or create a custom dialog
    var taskTime by remember { mutableStateOf("3") }
    var jiraNo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            onConfirm.invoke()
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = {
            Text("Confirmation")
        },
        text = {
            Card(
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Are you sure you want to approve?")
                    Text(item.staff_name!!)
                    Text(item.task_details)
                    Text(item.task_name)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = taskTime,
                        onValueChange = {
                            taskTime = it
                        },
                        label = { Text("Enter Task Time") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = jiraNo,
                        onValueChange = {
                            jiraNo = it
                        },
                        label = { Text("Enter Jira No") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        singleLine = true,
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {

                    if(userData.userType =="ADMIN"){

                        onConfirm.invoke()
                        taskAssignViewModel.addTaskAssign(
                            taskMappingRequest = TaskMappingRequest(
                                assigned_date = currentDateApi(),
                                project_id = item.project_id,
                                project_name = item.project_name,
                                staff_id = item.staff_id,
                                staff_name = item.staff_name!!,
                                task_id = item.id,
                                task_name = item.task_name,
                                task_status = "TO DO",
                                updated_date = currentDateApi(),
                                task_details = item.task_details,
                                taskTime = taskTime
                            ), onSuccess = {

                                if (userData.userType == "USER") {
                                    viewModel.getNewTaskList(staffName = userData.staff_name!!)
                                } else {
                                    viewModel.getNewTaskList(staffName = "ALL")
                                }

                                println("Approved success")

                            }
                        )

                        val userName = item.staff_name
                        val listStaff: ArrayList<String> = ArrayList()
                        taskAssignViewModel.state.value.staffList!!.forEach {
                            listStaff.add(element = it.staff_name)
                        }

                        val indexStaff = listStaff.indexOf(userName)
                        val selectedStaffId = taskAssignViewModel.state.value.staffList!![indexStaff].id.toString()
                        val staffEmailId = taskAssignViewModel.state.value.staffList!![indexStaff].mobile_number.toString()

                        val mailBody =
                            "Hello $userName,\nNew task (${item.task_name}) Assigned to you in the project ${item.project_name},\nPlease check the task entryApp immediately." +
                                    "\n\nTask details : ${item.task_details}  \n\n\nWith Regards,\n\nGirish Kumar G"

                        //SEND MAIL TO EMPLOYEE
                        if (staffEmailId.isNotEmpty()) {
                            sendEmail(
                                subject = "Reg:New Task Assigned ${item.task_name}",
                                content = mailBody,
                                context = context,
                                toMail = staffEmailId,
                                onSuccess = {
                                    //isSendMail = true
                                }
                            )
                        }

                    }else{
                        Toast.makeText(context,"Access Denied!!",Toast.LENGTH_LONG).show()
                    }

                    // rest of the code remains unchanged
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                    // Dismiss the dialog or perform any other action
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowConfirmationDialogOld(onConfirm: () -> Unit,taskAssignViewModel:TaskAssignViewModel,
                           viewModel:TaskViewModel,
                           item:NewTaskItem,userData:LoginData,context:Context) {
    // Use AlertDialog or create a custom dialog
    var taskTime by remember { mutableStateOf("3") }
    var jiraNo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {8
            onConfirm.invoke()
        },
        title = { Text("Confirmation") },
        text = {
            Column {
                Text("Are you sure you want to approve?")
                Text(item.staff_name!!)
                Text(item.task_details)
                Text(item.task_name)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = taskTime,
                    onValueChange = {
                        taskTime = it
                    },
                    label = { Text("Enter Task Time") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = jiraNo,
                    onValueChange = {
                        jiraNo = it
                    },
                    label = { Text("Enter Jira No") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                    taskAssignViewModel.addTaskAssign(
                        taskMappingRequest = TaskMappingRequest(
                            assigned_date = currentDateApi(),
                            project_id = item.project_id,
                            project_name = item.project_name,
                            staff_id = item.staff_id,
                            staff_name = item.staff_name!!,
                            task_id = item.id,
                            task_name = item.task_name,
                            task_status = "TO DO",
                            updated_date = currentDateApi(),
                            task_details = item.task_details,
                            taskTime = taskTime
                        ), onSuccess = {

                            if(userData!!.userType =="USER"){
                                viewModel.getNewTaskList(staffName = userData.staff_name!!)
                            }else{
                                viewModel.getNewTaskList(staffName = "ALL")
                            }

                            println("Approved success")

                        }
                    )

                    val userName = userData.staff_name
                    val toMail = userData.mobile_number
                    val mailBody = "Hello $userName,\nNew task (${item.task_name}) Assigned to you in the project ${item.project_name},\nPlease check the task entryApp immediately." +
                            "\n\nTask details : ${item.task_details}  \n\n\nWith Regards,\n\nGirish Kumar G"

                    //SEND MAIL TO EMPLOYEE
                    if(toMail.isNotEmpty()) {
                        sendEmail(
                            subject = "Reg:New Task Assigned ${item.task_name}",
                            content = mailBody,
                            context = context,
                            toMail = toMail,
                            onSuccess = {
                                //isSendMail = true
                            }
                        )
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                    // Dismiss the dialog or perform any other action
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ItemRow(data: NewTaskItem,onclick: (() -> Unit)) {
    // Define the UI for each item in the list
    // For example, you can create a Text element
    // that displays the item text.
    //Text(text, modifier = Modifier.padding(16.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
//                .height(130.dp)
                .clickable {

                    onclick.invoke()

                }
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_10),
                    horizontal = dimensionResource(id = R.dimen.dimen_10)
                ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
            elevation = 10.dp
        ) {

            if (data.project_name.isEmpty().not()) {
                Column(
                    modifier = Modifier
                        .background(
                            if (data.task_status == "TO DO") {
                                Color.White
                            } else {
                                doneColor
                            }
                        )
                        .padding(10.dp)
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
                            text = data.project_name!!,
                            style = TextStyle_600_12,
                            color = Color.Red
                        )

                        Text(
                            text = data.task_name!!,
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
                            text = formatDate(data.task_start_date!!),
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
                            text = "Task No",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = data.task_name!!,
                            style = TextStyle_500_12,
                            color = Color.Magenta
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
                            text = data.task_details!!,
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
                            text = "Status",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(70.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = "Approval Pending",
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
                            text = "Requested By",
                            style = TextStyle_600_12,
                            color = ColorPrimary,
                            modifier = Modifier.width(90.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = data.staff_name!!,
                            style = TextStyle_500_12,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}


