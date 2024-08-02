package com.gjglobal.daily_task_entry.presentation.dashboard.more.jiraempty

import ShimmerEffectListView
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString.*
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
import com.gjglobal.daily_task_entry.domain.domain.model.task.TaskJiraEmptyData
import com.gjglobal.daily_task_entry.domain.domain.model.task.edittaskentry.UpdateJiraRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.task.TaskViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.lightestBlue
import com.gjglobal.daily_task_entry.presentation.utils.formatDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskJiraEmptyScreen(
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
    var addTaskVisible by remember { mutableStateOf(false) }
    val showConfirmation by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select") }

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                viewModel.getProjects()

                if(userData?.userType=="ADMIN"){
                    viewModel.getJiraEmptyTaskList(projectName = "0", staffName = "0")
                }else{
                    viewModel.getJiraEmptyTaskList(projectName = "0", staffName = userData?.staff_name!!)
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
            ToolBar(nameOfScreen = "JIRA Empty Task List", iconOfScreen = 0, onClick = {
                if (addTaskVisible){
                    addTaskVisible = false
                }else {
                    navController.popBackStack()
                }
            }, onIconClick = {

            })
        },
       content = {

           var showSuccess by remember { mutableStateOf(false) }
           val listStatusItems =ArrayList<String>()
           state.projectList?.forEach {
               listStatusItems.add(it.project_name)
           }

           Box(
               modifier = Modifier.fillMaxSize()
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
                                                   if(userData?.userType=="ADMIN"){
                                                       viewModel.getJiraEmptyTaskList(projectName = selectedStatus, staffName = "0")
                                                   }else{
                                                       viewModel.getJiraEmptyTaskList(projectName = selectedStatus, staffName = userData?.staff_name!!)
                                                   }
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
                       }
                   }


               if (state.isGiraTaskList!!) {
                   LazyColumnExample(
                       state.emptyJiraList!!, taskAssignViewModel = taskAssignViewModel,
                       taskViewModel = viewModel,
                       userData = userData!!, context = context, projectName = selectedStatus
                   )
               } else {
                   if (state.isLoading) {
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.Center,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           ShimmerEffectListView(state.isLoading)
                           //CircularProgressIndicator(modifier = Modifier.padding(vertical = 20.dp))
                       }

                   }
               }

           }
               }

           if (showSuccess) {
               Messagebox(onSuccess = {
                   showSuccess = false
                   //onCancelClick.invoke()
               }, message = "Task Jira number updated!!")
           }
       }
    )
}



@Composable
fun LazyColumnExample(data:List<TaskJiraEmptyData>, taskAssignViewModel: TaskAssignViewModel, taskViewModel:TaskViewModel, userData: LoginData, context: Context,projectName: String) {
    var showDialog by remember { mutableStateOf(false)}
    var selectedItem by remember { mutableStateOf<TaskJiraEmptyData?>(null) }

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
        }, taskAssignViewModel = taskAssignViewModel, viewModel =taskViewModel, item = selectedItem!!,userData = userData , context =context ,projectName= projectName)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowConfirmationDialog(onConfirm: () -> Unit,taskAssignViewModel:TaskAssignViewModel,
                           viewModel:TaskViewModel,
                           item:TaskJiraEmptyData,userData:LoginData,context:Context,projectName :String) {
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
                    Text("Are you sure you want to update JIRA No?")
                    Text(item.task_details)
                    Text(item.task_name)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = jiraNo,
                        onValueChange = {
                            jiraNo = it.toUpperCase() // Convert the input to uppercase
                        },
                        label = { Text("Enter Jira No") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            autoCorrect = false // Disable auto-correction to prevent lowercase suggestions
                        ),
                        singleLine = true,
                        //visualTransformation = UppercaseVisualTransformation() // Transform input to uppercase
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()

                    viewModel.updateJira(

                        updateJiraRequest = UpdateJiraRequest(
                            jiraNo ="${item.jiraProjCode}-$jiraNo",
                            task_name = item.task_name

                        ),id=item.id, onSuccess = {
                            if(userData.userType =="ADMIN"){
                                viewModel.getJiraEmptyTaskList(projectName = "0", staffName = "0")
                            }else{
                                viewModel.getJiraEmptyTaskList(projectName = "0", staffName = userData.staff_name!!)
                            }
                            println(" success")

                        }
                    )

//                    val userName = userData.staff_name
//                    val toMail = userData.mobile_number
//                    val mailBody =
//                        "Hello $userName,\nNew task (${item.task_name}) Assigned to you in the project ${item.project_name},\nPlease check the task entryApp immediately." +
//                                "\n\nTask details : ${item.task_details}  \n\n\nWith Regards,\n\nGirish Kumar G"
//
//                    //SEND MAIL TO EMPLOYEE
//                    if (toMail.isNotEmpty()) {
//                        sendEmail(
//                            subject = "Reg:New Task Assigned ${item.task_name}",
//                            content = mailBody,
//                            context = context,
//                            toMail = toMail,
//                            onSuccess = {
//                                //isSendMail = true
//                            }
//                        )
//                    }
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




@Composable
fun ItemRow(data: TaskJiraEmptyData,onclick: (() -> Unit)) {
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
                            text = data.task_status,
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
                        data.created_by?.let {
                            Text(
                                text = it,
                                style = TextStyle_500_12,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}


