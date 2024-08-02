package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.taskview

import ShimmerEffectListView
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequestNew
import com.gjglobal.daily_task_entry.domain.domain.model.staff.StaffData
import com.gjglobal.daily_task_entry.domain.domain.model.task.recentupdateqa.RecentUpdateQaRequest
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.components.TaskCard
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.task.TaskViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreenColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.utils.formatDate

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskListViewScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: TaskListViewModel = hiltViewModel(),
    taskStatus:String
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    var staffName = userData?.staff_name
    val taskAssignViewModel: TaskAssignViewModel = hiltViewModel()
    val taskAssignViewModelState = taskAssignViewModel.state.value
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Select") }
    var searchText by remember { mutableStateOf("") }

    val taskViewModel: TaskViewModel = hiltViewModel()

    //val taskStatus :String = "COMPLETED"

    var taskListRequest : TaskListRequestNew?= null
    val qaStaffList: List<StaffData>?

    if(userData!!.userType != "QA"){

        if(userData.userType=="ADMIN"){
            staffName = "ALL"
            taskListRequest = if(selectedStatus =="Select" || selectedStatus.isEmpty()){
                TaskListRequestNew(staff_name = "ALL", task_status = taskStatus, staff_type = "SW", project_name = "ALL")
            }else{
                TaskListRequestNew(staff_name = "ALL", task_status = taskStatus, staff_type = "SW", project_name = selectedStatus)
            }

        }else{
            taskListRequest = TaskListRequestNew(staff_name = staffName!!, task_status = taskStatus, staff_type = "SW" ,project_name = "ALL")
        }

    }else{

        taskListRequest = TaskListRequestNew(staff_name = staffName!!, task_status = taskStatus, staff_type = "QA",project_name = "ALL")
    }

    var showSuccess by remember { mutableStateOf(false) }

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                taskViewModel.getProjects()
                taskAssignViewModel.getStaffs()

                viewModel.getTaskListNew(
                    taskListRequest = taskListRequest!!
                )

                viewModel.getRecentQaUpdates(
                    RecentUpdateQaRequest(
                        staff_name = staffName,
                        limit_count = "10",
                    )
                )

                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }


    }

    qaStaffList  = cacheManager.getQAStaffData()

    //qaStaffList = null

    var listStatusItems = viewModel.state.value.taskList
        ?.map { it.project_name }
        ?.distinct()
        ?.toMutableList() ?: mutableListOf()


    println("qa_staffs $qaStaffList")


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            ToolBar(nameOfScreen = "$taskStatus Tasks",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})
            Spacer(modifier = Modifier.width(20.dp))


            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText

                    viewModel.setSearchText(searchText)
                    //viewModel.searchText = newText
                    // Handle search logic here
                },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            /*Row(
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
                                    listStatusItems.clear()
                                    listStatusItems = viewModel.state.value.taskList
                                        ?.map { it.project_name }
                                        ?.distinct()
                                        ?.toMutableList() ?: mutableListOf()

                                    expandedStatus = !expandedStatus
                                }) {
                                ExposedDropdownMenu(expanded = expandedStatus,
                                    onDismissRequest = {
                                        expandedStatus = false
                                    }) {
                                    listStatusItems.forEach { selectedOption ->
                                        DropdownMenuItem(onClick = {
                                            selectedStatus = selectedOption

                                            if (userData.userType != "QA") {

                                                if (userData.userType == "ADMIN") {
                                                    staffName = "ALL"
                                                    taskListRequest = TaskListRequestNew(
                                                        staff_name = "ALL",
                                                        task_status = taskStatus,
                                                        staff_type = "SW",
                                                        project_name = selectedStatus
                                                    )
                                                } else {
                                                    taskListRequest = TaskListRequestNew(
                                                        staff_name = staffName!!,
                                                        task_status = taskStatus,
                                                        staff_type = "SW",
                                                        project_name = selectedStatus
                                                    )
                                                }

                                            } else {

                                                taskListRequest = TaskListRequestNew(
                                                    staff_name = staffName!!,
                                                    task_status = taskStatus,
                                                    staff_type = "QA",
                                                    project_name = selectedStatus
                                                )
                                            }
                                            viewModel.getTaskListNew(
                                                taskListRequest = taskListRequest!!
                                            )

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
            }*/

            if (viewModel.state.value.isLoading) {

                ShimmerEffectListView(isLoading = viewModel.state.value.isLoading)

            }else{
                var qaEnable :Boolean = false
                if(taskStatus=="In QA Testing"){
                    qaEnable = true
                }

                val taskList1 by viewModel.taskList.collectAsState()

                if (taskList1.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(taskList1) { item ->
                            TaskCard( modifier = Modifier,
                                onClick = {
                                    showSuccess = true
                                }, list = item,
                                viewModel, buttonEnable = false,
                                taskStatus = taskStatus
                            , onStatusUpdate = {
                                    showSuccess = true
                                    viewModel.getTaskListNew(
                                        taskListRequest = taskListRequest!!
                                    )

                                    viewModel.getRecentQaUpdates(
                                        RecentUpdateQaRequest(
                                            staff_name = staffName!!,
                                            limit_count = "20",
                                        )
                                    )
                                }, qaEnable = qaEnable, staffList = qaStaffList)
                        }
                        item {
                            //Spacer(modifier = Modifier.height(10.dp))
                            println("qaEnable$qaEnable")
                            println(viewModel.state.value.isRecentUpdatesQaList!!)
                            if(qaEnable){
                                InProgressQaList(viewModel=viewModel, navController = navController)
                            }
                        }
                    }
                }else{
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable {}, contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
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
                }

            }
        }
    }

    if(showSuccess){
        Messagebox(onSuccess = {
            showSuccess = false
            viewModel.getTaskListNew(
                taskListRequest = taskListRequest!!
            )
           // navController.navigate(Screen.TaskListToDoViewScreen.route)

        }, message = "Status updated!!" )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InProgressQaList(viewModel: TaskListViewModel,navController: NavController) {

    val leaveList1 by viewModel.recentUpdatesQaList.collectAsState()
    val context = LocalContext.current

    if (viewModel.state.value.isRecentUpdatesQaList!!) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding()
        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Text(text = "Recent QA Updates", style = TextStyle_600_14, color = ColorPrimary)
                }
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn(modifier = Modifier.height(600.dp)) {
                    items(leaveList1) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    //.height(130.dp)
                                    .clickable {

                                    }
                                    .padding(
                                        vertical = dimensionResource(id = R.dimen.dimen_10),
                                        horizontal = dimensionResource(id = R.dimen.dimen_10)
                                    ),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
                                elevation = 10.dp
                            ) {

                                if (item.project_name.isNullOrBlank().not()) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                if (item.qa_status!! == "QA Testing Passed") {
                                                    doneColor
                                                } else {
                                                    inProgressColor
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
                                                text = item.project_name!!,
                                                style = TextStyle_600_12,
                                                color = Color.Red
                                            )

                                            Text(
                                                text = item.qa_task_no!!,
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
                                                text = formatDate(item.qa_date!!),
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
                                                text = "Jira Id",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            item.qa_jira_no?.let {
                                                Text(
                                                    text = it,
                                                    style = TextStyle_600_14,
                                                    color = Color.Magenta
                                                )
                                            }
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
                                                text = item.task_no!!,
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
                                                text = "Task",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.task_details!!,
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
                                                text = "Level",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.completed_level!!+ " % done",
                                                style = TextStyle_500_12,
                                                color = if(item.qa_status=="QA Testing Progress"){
                                                    Color.Red}else{
                                                    DarkGreenColor
                                                }
                                            )
                                        }



                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                            )
                                        ) {
                                            Text(
                                                text = "Jira Id",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(100.dp)
                                            )

                                            Text(
                                                text = item.qa_jira_no!!,
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
                                                text = "QA Remark",
                                                style = TextStyle_600_12,
                                                color = ColorPrimary,
                                                modifier = Modifier.width(70.dp)
                                            )
                                            Spacer(modifier = Modifier.width(30.dp))
                                            Text(
                                                text = item.qa_remarks!!,
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
                                                text = item.qa_status,
                                                style = TextStyle_500_12,
                                                color = ColorPrimary
                                            )
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

    } else {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (viewModel.state.value.isLoading) {
                Text(
                    text = "Loading...",
                    style = TextStyle_400_14,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
        }

    }

}
