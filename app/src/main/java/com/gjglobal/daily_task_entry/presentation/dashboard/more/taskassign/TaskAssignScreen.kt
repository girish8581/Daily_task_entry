package com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskMappingRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.task.TaskViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApiReport
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskAssignScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskAssignViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel= hiltViewModel()

) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val taskState = taskViewModel.state.value
    val state = viewModel.state.value

    var showSuccess by remember { mutableStateOf(false) }

    var selectedFromDate = ""

    var selectedStaffId by remember {
        mutableStateOf("")
    }
    var selectedProjectId by remember {
        mutableStateOf("")
    }

    var selectedTaskid by remember {
        mutableStateOf("")
    }

    var selectedTaskDetails by remember {
        mutableStateOf("")
    }

    var selectedTaskStatus by remember {
        mutableStateOf("")
    }


    var textFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }

    val pYear: Int
    val pMonth: Int
    val pDay: Int
    val calendar = Calendar.getInstance()
    pYear = calendar.get(Calendar.YEAR)
    pMonth = calendar.get(Calendar.MONTH)
    pDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf(currentDateApiReport()) }

    var sYear: String
    var sMonth: String
    var sDay: String
    val focusRequester = remember { FocusRequester() }
    val focusRequesterForPassword by remember { mutableStateOf(FocusRequester()) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            sYear = year.toString()
            sMonth = (month + 1).toString()
            Log.e("month", month.toString())
            sDay = dayOfMonth.toString()

            if (sMonth.length == 1) sMonth = "0$sMonth"
            if (sDay.length == 1) sDay = "0$sDay"

            date.value = "$sYear/$sMonth/$sDay"
        }, pYear, pMonth, pDay
    )
    Log.e("fromDate", date.toString())

    var expanded by remember {
        mutableStateOf(false)
    }
    var expandedExit by remember {
        mutableStateOf(false)
    }
    var expandedVehicle by remember {
        mutableStateOf(false)
    }

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                taskViewModel.getProjects()
                viewModel.getStaffs()
                //viewModel.getTasks()
                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }

    val listStaff: ArrayList<String> = ArrayList()

    val listProject: ArrayList<String> = ArrayList()

    val listTask: ArrayList<String> = ArrayList()

    val listVehicleItems = ArrayList(listOf("Car", "Bike"))

    var selectedStaff by remember {
        mutableStateOf("Select  Staff")
    }

    var selectedProject by remember {
        mutableStateOf("Select  Project")
    }

    var selectedTask by remember {
        mutableStateOf("Select task")
    }

    state.staffList?.forEach {
        listStaff.add(it.staff_name)
    }

    if(state.isTaskList!!){
        state.taskList?.forEach {
            listTask.add(it.task_name)
        }
    }


    taskState.projectList?.forEach{
        listProject.add(it.project_name)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        // verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        ToolBar(nameOfScreen = "Assign Task", iconOfScreen = 0, onClick = {
            navController.popBackStack()
        }, onIconClick = {})

        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                Spacer(modifier = Modifier.height(15.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(id = R.dimen.dimen_10),
                            horizontal = dimensionResource(id = R.dimen.dimen_10)
                        ),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_20)),
                ) {
                    Column(

                        modifier = Modifier.background(lightestBlue)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = "Staff:",
                                modifier = Modifier.width(100.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .border(
                                        0.5.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(4.27.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
                                    expanded = !expanded
                                }) {
                                    var indexStaff: Int = -1
                                    ExposedDropdownMenu(expanded = expanded,
                                        onDismissRequest = { expanded = false }) {
                                        listStaff.forEach { selectedOption ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedStaff = selectedOption
                                                    indexStaff = listStaff.indexOf(selectedStaff)
                                                    selectedStaffId =
                                                        state.staffList?.get(indexStaff)?.id.toString()

                                                    println("$selectedStaff $selectedStaffId")

                                                    expanded = false
                                                }) {
                                                Text(
                                                    text = selectedOption
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
                                            text = selectedStaff,
                                            color = Color.Black,
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

                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = "Project:",
                                modifier = Modifier.width(100.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .border(
                                        0.5.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(4.27.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                ExposedDropdownMenuBox(expanded = expandedExit, onExpandedChange = {
                                    expandedExit = !expandedExit

                                }) {
                                    var index: Int = -1
                                    ExposedDropdownMenu(expanded = expandedExit,
                                        onDismissRequest = { expandedExit = false }) {
                                        listProject.forEach { selectedOption1 ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedProject = selectedOption1
                                                    index = listProject.indexOf(selectedProject)
                                                    selectedProjectId =
                                                        taskState.projectList?.get(index)?.id.toString()
                                                    state.taskList = null
                                                    state.isTaskList = false
                                                    viewModel.getTasksProjectName(selectedProject)
                                                    selectedTask = ""
                                                    expandedExit = false
                                                }) {
                                                Text(
                                                    text = selectedOption1
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
                                                expandedExit = true
                                            },

                                        ) {
                                        Text(
                                            text = selectedProject,
                                            color = Color.Black
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


                        Spacer(modifier = Modifier.height(30.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = "Task:",
                                modifier = Modifier.width(100.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            if (state.isTaskLoading!!) {
                                AnimatedVisibility(visible = state.isTaskList!!.not()) {
                                    CircularProgressIndicator(
                                        color = ColorPrimary,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            0.5.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(4.27.dp)
                                        )
                                        .fillMaxWidth()
                                        .height(40.dp)
                                ) {
                                    ExposedDropdownMenuBox(
                                        expanded = expandedVehicle,
                                        onExpandedChange = {
                                            expandedVehicle = !expandedVehicle
                                        }) {
                                        var index: Int = -1
                                        ExposedDropdownMenu(expanded = expandedVehicle,
                                            onDismissRequest = { expandedVehicle = false }) {
                                            listTask.forEach { selectedOption ->
                                                DropdownMenuItem(
                                                    onClick = {
                                                        selectedTask = selectedOption
                                                        state.isTaskData = false
                                                        state.taskData = null
                                                        index = listTask.indexOf(selectedTask)
                                                        selectedTaskid =
                                                            state.taskList?.get(index)?.id.toString()
                                                        selectedTaskDetails =
                                                            state.taskList?.get(index)?.task_details.toString()
                                                        selectedTaskStatus =
                                                            state.taskList?.get(index)?.task_status.toString()
                                                        //viewModel.getTaskData(selectedTask)
                                                        expandedVehicle = false
                                                    }) {
                                                    Text(
                                                        text = selectedOption
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
                                                    expandedVehicle = true
                                                },

                                            ) {
                                            Text(
                                                text = selectedTask,
                                                color = Color.Black
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
                        Spacer(modifier = Modifier.height(30.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = "Remarks:",
                                modifier = Modifier.width(100.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = selectedTaskDetails,
                                modifier = Modifier.fillMaxWidth()
                                    .fillMaxWidth(1.0f)

                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = "Date : ",
                                modifier = Modifier.width(100.dp)
                            )


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                selectedFromDate = date.value
                                Text(
                                    text = selectedFromDate,
                                )
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

                        OutlinedTextField(
                            value = if(state.isValidTaskTime!!){state.taskTime!!}else{""},
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start),
//                                .focusRequester(focusRequester),
                            onValueChange = {
                                viewModel.isValidTaskTime(it)
                            },
                            textStyle = TextStyle_500_16,
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                                onNext = {  }),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            singleLine = true,
                            label = { Text(text = "Task Time") },
                            placeholder = { Text(text = "Enter Task Time") },
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Button(
                                onClick = {
                                    if (selectedTask != "Select Task" && selectedProjectId.isNotEmpty() && selectedTaskid.isNotEmpty() && selectedFromDate.isNotEmpty() && state.isValidTaskTime!!) {

                                        viewModel.addTaskAssign(
                                            taskMappingRequest = TaskMappingRequest(
                                                assigned_date = selectedFromDate,
                                                project_id = selectedProjectId,
                                                project_name = selectedProject,
                                                staff_id = selectedStaffId,
                                                staff_name = selectedStaff,
                                                task_id = selectedTaskid,
                                                task_name = selectedTask,
                                                task_status = "TO DO",
                                                updated_date = currentDateApi(),
                                                task_details = selectedTaskDetails,
                                                taskTime = state.taskTime!!
                                            ), onSuccess = {
                                                selectedFromDate = ""
                                                selectedProjectId = ""
                                                selectedProject = "Select Project"
                                                selectedStaffId = ""
                                                selectedStaff = "Select Staff"
                                                selectedTaskid = ""
                                                selectedTask = "Select Task"
                                                selectedTaskStatus = ""
                                                selectedTaskDetails = ""
                                                showSuccess = true
                                                viewModel.isValidTaskTime("0")
                                            }
                                        )

                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please select project and staff etc..!!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(40.dp),
                                shape = RoundedCornerShape(8.dp),
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
            }
        }
    }
            if (showSuccess) {
                Messagebox(onSuccess = {
                    showSuccess = false
                    //onCancelClick.invoke()
                }, message = "New Task assigned!!")
            }

    if(state.isAlreadyAssigned!!){
        Toast.makeText(context,"Task already assigned!!",Toast.LENGTH_LONG).show()
    }
}