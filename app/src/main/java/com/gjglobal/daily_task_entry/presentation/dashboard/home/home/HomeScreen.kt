package com.gjglobal.daily_task_entry.presentation.dashboard.home.home

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.core.Constants
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.StaffTaskDateWiseRequest
import com.gjglobal.daily_task_entry.domain.domain.model.requestmodel.TaskListRequest
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.presentation.components.MyWorker
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.components.AutoScrollingLazyRow
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components.SummaryReportGraph
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.notification.NotificationViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BlueWhite
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_700_20
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import kotlinx.coroutines.delay
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: HomeScreenViewModel= hiltViewModel(),
    notificationViewModel:NotificationViewModel = hiltViewModel()
) {
    val taskViewModel : TaskAssignViewModel = hiltViewModel()
    val taskListViewModel : TaskListViewModel = hiltViewModel()

    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val workManager = WorkManager.getInstance()
   // val workManager = WorkManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val designation = userData?.designation
    val notificationState = notificationViewModel.state
    val currentTime = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }

    val offsetX by remember { mutableStateOf(0f) }
    val direction by remember { mutableStateOf(1f) }
    val id = userData?.ImageId
    //Profile image loading url

    val url  = Constants.BASE_URL+"/services/image_api.php?id=$id"
    val profileImageUrl by remember { mutableStateOf(url) }
    var notificationAlert by remember { mutableStateOf(true) }
    var showGraph by remember { mutableStateOf(true) }

    // Calculate the start of the week based on the day of the week
    val selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val weekStartDate = selectedDate.with(DayOfWeek.MONDAY)
    val weekEndDate = selectedDate.with(DayOfWeek.SUNDAY)

    val userRole = userData?.userType

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    var taskListRequest : TaskListRequest?= null

    taskListRequest = TaskListRequest(staff_name = staffName!!, task_status = "IN PROGRESS")


    var profileImage: File? by remember { mutableStateOf(null) }

    val state = viewModel.state.value
   val taskListState = taskListViewModel.state.value

    val cardList = mutableListOf<CardItem>()
//    cardList.add(CardItem("project", "task"))
//    cardList.add(CardItem("Project1", "task1"))
//    cardList.add(CardItem("Project2", "task1"))
//    cardList.add(CardItem("Project3", "task1"))


    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                cacheManager.clearNotificationStatusIfDateChanged()

                taskViewModel.getStaffs()

                taskListViewModel.getTaskList(
                    taskListRequest = taskListRequest
                )
                viewModel.gatTasksCount(taskCountSummaryRequest(
                    staff_name = staffName
                ))

                dashViewModel.hideBottomMenu(false)
                viewModel.downloadProfilePicture(userData.ImageId.toInt(),activity)

                if(userData.userType =="ADMIN"){
                    notificationViewModel.getNotificationAdmin()
                }else{
                    notificationViewModel.getNotification(userName = staffName)
                }

//                val periodicWorkRequest = PeriodicWorkRequest.Builder(
//                    MyWorker::class.java,
//                    15, TimeUnit.MINUTES
//                ).build()
//                // Enqueue the periodic work request
//                workManager.enqueue(periodicWorkRequest)

                notificationViewModel.updatePrevCount(cacheManager=cacheManager)

               // Log.e("notfnStts",notificationState.value.isNotificationList.toString())


            }

            Lifecycle.Event.ON_STOP -> {

                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }

    DoBackPress(context = context)

    if(taskListState.isTaskList!!){
        taskListState.taskList?.forEach {
            cardList.add(CardItem(
                title = it.project_name, subtitle = it.task_details
            ))
        }
    }

    if(taskViewModel.state.value.isStaffList!!){

        taskViewModel.state.value.staffList?.let { cacheManager.saveStaffData(it) }

    }


    val painter = painterResource(id = R.drawable.back_1)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)

    ) {
        Image(
            painter = painter,
            contentDescription = null, // Provide an appropriate description
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust content scale if needed
        )

        Box {
            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_10)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_icons),
//                            contentDescription = "doctor image",
//                            modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_50))
//                        )

                        ImageFromApi(viewModel)

                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.dimen_16)))
                        Column {
                            Text(
                                text = when {
                                    currentTime < 12 -> "Good Morning 👋"
                                    currentTime < 16 -> "Good Afternoon 👋"
                                    else -> "Good Evening 👋"
                                },
                                style = TextStyle_400_16
                            )
                                Text(
                                    text = userData?.staff_name!!,
                                    style = TextStyle_700_20
                                )

                        }
                    }

                    if(notificationState.value.isNotificationList) {
                        notificationViewModel.notificationManager(
                            notificationState.value.prevNotificationCount,
                            notificationState.value.notificationList!!,
                            context
                        )
                    }

                   // Log.e("notfnStts Screen",notificationState.value.isNotificationList.toString())


                    if(notificationState.value.isNewNotification!!){
                    Image(
                        painter = painterResource(id = R.drawable.bell_badge),
                        contentDescription = "notifications",
                        modifier = Modifier.clickable{
                            navController.navigate(Screen.NotificationScreen.route)
                        }
                    )}else{
                        Image(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "notifications",
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.NotificationScreen.route)
                            })

                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_20)))
                Text(
                    text = "My Tasks", style = TextStyle_800_18, color = ColorPrimary,modifier = Modifier.padding(horizontal = 20.dp)
                )



                //AutoScrollingCardList(cards =cardList)


//                if(cardList.isNotEmpty()){
//                    //var index = Int
//                    AutoScrollingLazyRow(list = cardList, modifier = Modifier.height(100.dp)) {
//                        //LazyListItem(text = "Item $it")
//                        AutoScrollingCard(it)
//                        viewModel.setDotIndex(cardList.indexOf(it))
//
//                    }
//                    DotIndicator(itemCount = cardList.size, currentPage = state.indexDot!!, modifier = Modifier.fillMaxWidth())
//
//                }



                if(userRole != "QA") {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .clickable {
                                    notificationAlert = true
                                    navController.navigate(Screen.TaskListToDoViewScreen.route)
                                }
                                .weight(1f), // Use weight to distribute available space equally
                            shape = RoundedCornerShape(10.dp),
                            elevation = 10.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                    .background(Color.White),
                                contentAlignment = Alignment.Center // Align the content in the center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(Modifier.padding(10.dp)) {
                                        Image(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp),
                                            painter = painterResource(id = R.drawable.todo_icon),
                                            contentDescription = "Subscriber icon"
                                        )
                                    }

                                    Text(
                                        text = "Todo",
                                        modifier = Modifier,
                                        color = ColorPrimary,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )

                                    if (state.isTaskCount!!) {
                                        if (state.taskCount?.get(0)?.todoCount.isNullOrBlank()
                                                .not()
                                        ) {
                                            Text(
                                                text = if (state.isTaskCount!!) {
                                                    state.taskCount?.get(0)?.todoCount!! + " Tasks"
                                                } else {
                                                    "0 Tasks"
                                                }, style = TextStyle_600_12,
                                                color = if (state.taskCount?.get(0)?.todoCount!!.toInt() != 0) {
                                                    Color.Red
                                                } else {
                                                    DarkGreen
                                                },
                                                modifier = Modifier
                                                    .offset(
                                                        x = if (state.taskCount?.get(0)?.todoCount!!.toInt() != 0) {
                                                            animatedOffsetX.dp
                                                        } else {
                                                            0.dp
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                }
                            }

                        }

                        Spacer(modifier = Modifier.width(20.dp)) // Add spacing between the cards

                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .weight(1f)
                                .clickable {

                                    navController.navigate(Screen.TaskListScreen.route)
                                }, // Use weight to distribute available space equally
                            shape = RoundedCornerShape(10.dp),
                            elevation = 10.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                    .background(Color.White),
                                contentAlignment = Alignment.Center // Align the content in the center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(Modifier.padding(10.dp)) {
                                        Image(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp),
                                            painter = painterResource(id = R.drawable.inprogress),
                                            contentDescription = "Subscriber icon"
                                        )
                                    }

                                    Text(
                                        text = "In Progress",
                                        modifier = Modifier,
                                        color = ColorPrimary,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )

                                    if (state.isTaskCount && state.taskCount?.get(0)?.inProgressCount != null && state.taskCount!![0]?.inProgressCount!!.toInt() != 0) {
                                        Text(
                                            text = state.taskCount!![0]?.inProgressCount.toString() + " Tasks",
                                            style = TextStyle_600_12,
                                            color = Color.Red,
                                            modifier = Modifier.offset(animatedOffsetX.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "0 Tasks",
                                            style = TextStyle_600_12,
                                            color = DarkGreen,
                                            modifier = Modifier.offset(0.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .weight(1f)
                            .clickable {
                                navController.navigate(Screen.TaskListQaReadyViewScreen.route)
                            },
                        // Use weight to distribute available space equally
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                .background(Color.White),
                            contentAlignment = Alignment.Center // Align the content in the center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.padding(10.dp)) {
                                    Image(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(40.dp),
                                        painter = painterResource(id = R.drawable.qa_icon),
                                        contentDescription = "qa icon"
                                    )
                                }

                                Text(
                                    text = "In Quality Assurance",
                                    modifier = Modifier,
                                    color = ColorPrimary,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                                if (state.taskCount?.get(0)?.inQaTesting.isNullOrBlank().not()) {
                                    if (state.isTaskCount) {
                                        Text(
                                            text = if (state.isTaskCount) {
                                                state.taskCount?.get(0)?.inQaTesting!! + " Tasks"
                                            } else {
                                                "0 Tasks"
                                            }, style = TextStyle_600_12,
                                            color = if (state.taskCount?.get(0)?.inQaTesting!!.toInt() != 0) {
                                                Color.Red
                                            } else {
                                                DarkGreen
                                            },
                                            modifier = Modifier
                                                .offset(
                                                    x = if (state.taskCount?.get(0)?.inQaTesting!!.toInt() != 0) {
                                                        animatedOffsetX.dp
                                                    } else {
                                                        0.dp
                                                    }
                                                )
                                        )
                                    }
                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.width(20.dp)) // Add spacing between the cards

                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .weight(1f)
                            .clickable {
                                navController.navigate(Screen.TaskListCompleteViewScreen.route)
                            }, // Use weight to distribute available space equally
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                .background(Color.White),
                            contentAlignment = Alignment.Center // Align the content in the center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.padding(10.dp)) {
                                    Image(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(40.dp),
                                        painter = painterResource(id = R.drawable.done_icon),
                                        contentDescription = "done icon"
                                    )
                                }

                                Text(
                                    text = "QA Passed",
                                    modifier = Modifier,
                                    color = ColorPrimary,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                if(state.taskCount?.get(0)?.completedCount.isNullOrBlank().not()) {
                                    if (state.isTaskCount!!) {
                                        Text(
                                            text = if (state.isTaskCount!!) {
                                                state.taskCount?.get(0)?.completedCount!! + " Tasks"
                                            } else {
                                                "0 Tasks"
                                            }, style = TextStyle_600_12,
                                            color = ColorPrimary
                                        )
                                    }
                                }
                            }

                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .weight(1f)
                            .clickable {
                                navController.navigate(Screen.LeaveScreen.route)
                            }, // Use weight to distribute available space equally
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                .background(Color.White),
                            contentAlignment = Alignment.Center // Align the content in the center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.padding(10.dp)) {
                                    Image(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(40.dp),
                                        painter = painterResource(id = R.drawable.leave),
                                        contentDescription = "qa icon"
                                    )
                                }

                                Text(
                                    text = "Leave",
                                    modifier = Modifier,
                                    color = ColorPrimary,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.width(20.dp)) // Add spacing between the cards

                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .weight(1f)
                            .clickable {
                                navController.navigate(Screen.TaskScreen.route)
                            }, // Use weight to distribute available space equally
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                .background(Color.White),
                            contentAlignment = Alignment.Center // Align the content in the center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.padding(10.dp)) {
                                    Image(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(40.dp),
                                        painter = painterResource(id = R.drawable.assign_task),
                                        contentDescription = "done icon"
                                    )
                                }

                                Text(
                                    text = "Add New Task",
                                    modifier = Modifier,
                                    color = ColorPrimary,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }


                //if(userData.userType=="QA"){
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .weight(1f)
                                .clickable {
                                    navController.navigate(Screen.OtherJobScreen.route)
                                },
                            shape = RoundedCornerShape(10.dp),
                            elevation = 10.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                    .background(Color.White),
                                contentAlignment = Alignment.Center // Align the content in the center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(Modifier.padding(10.dp)) {
                                        Image(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp),
                                            painter = painterResource(id = R.drawable.extra_job),
                                            contentDescription = "qa icon"
                                        )
                                    }

                                    Text(
                                        text = "Other Jobs",
                                        modifier = Modifier,
                                        color = ColorPrimary,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }

                        Spacer(modifier = Modifier.width(20.dp)) // Add spacing between the cards

                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .weight(1f)
                                .clickable {
                                    //navController.navigate(Screen.TaskScreen.route)
                                }, // Use weight to distribute available space equally
                            shape = RoundedCornerShape(10.dp),
                            elevation = 10.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize() // This makes the Box take up the entire space inside the Card
                                    .background(Color.White),
                                contentAlignment = Alignment.Center // Align the content in the center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(Modifier.padding(10.dp)) {
                                        Image(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp),
                                            painter = painterResource(id = R.drawable.assign_task),
                                            contentDescription = "done icon"
                                        )
                                    }

                                    Text(
                                        text = "--",
                                        modifier = Modifier,
                                        color = ColorPrimary,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                //}


                      LaunchedEffect(Unit){

                                if(userRole == "ADMIN") {
                                    taskViewModel.getStaffTaskDateWise(
                                        StaffTaskDateWiseRequest(
                                            from_date = weekStartDate.toString(),
                                            staff_name = "ALL",
                                            to_date = weekEndDate.toString()
                                        )
                                    )
                                }else{
                                    taskViewModel.getStaffTaskDateWise(
                                        StaffTaskDateWiseRequest(
                                            from_date = weekStartDate.toString(),
                                            staff_name = staffName!!,
                                            to_date = weekEndDate.toString()
                                        )
                                    )
                                }
                                showGraph = true
                            }

            }
        }
    }
//    if(showGraph){
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Column(
//                modifier = Modifier.align(Alignment.BottomEnd)
//            ) {
//                SummaryReportGraph(viewModel=taskViewModel, onClick = {
//                },weekStartDate=weekStartDate.toString(), weekEndDate = weekEndDate.toString(),
//                    userRole = userRole.toString(), staffName = staffName.toString())
//            }
//        }
//    }

    if (showGraph) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.Bottom

            ) {
                SummaryReportGraph(
                    viewModel = taskViewModel,
                    onClick = {
                        // Handle the onClick action
                    },
                    weekStartDate = weekStartDate.toString(),
                    weekEndDate = weekEndDate.toString(),
                    userRole = userRole.toString(),
                    staffName = staffName.toString()
                )
            }
        }
    }
}

@Composable
fun BoxRowNew(
    text: String,
    imageResId: Int,
    onclick: (() -> Unit)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(

            modifier = Modifier
                .clickable { (onclick.invoke()) }
                .background(
                    BlueWhite,
                    shape = RoundedCornerShape(30.dp)
                )
                .size(56.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "calender logo",
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = text,
            style = TextStyle_500_14,
            textAlign = TextAlign.Center,
            modifier = Modifier.height(45.dp)
        )
    }
}

@Composable
fun ProfileImagePickerNew(
    onSaveImage: (File) -> Unit
) {
    var selectedImage: File? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // Handle the selected image URI
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val imageFile = File(context.filesDir, "profile_image.png")
            inputStream?.use { input ->
                imageFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            selectedImage = imageFile
            onSaveImage(imageFile)
        }
    }

            if (selectedImage != null) {
                Image(
                    painter = rememberImagePainter(data = selectedImage,
                        builder = {
                            transformations(CircleCropTransformation())
                        }),
                    contentDescription = "Profile Image",

                    modifier = Modifier
                        .size(90.dp)
                        .padding(10.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.profile_icons),
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(10.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
    }
}

@Composable
fun ImageFromApi(viewModel: HomeScreenViewModel) {

    if(viewModel.state.value.isLoading){
        AnimatedVisibility(visible = true) {
            CircularProgressIndicator(
                color = ColorPrimary,
                modifier = Modifier
                    .size(40.dp),
                strokeWidth = 2.dp
            )
        }
    }else{
        val painter: Painter = if (viewModel.selectedImageUri.value != null) {
            rememberImagePainter(viewModel.selectedImageUri.value)
        } else if (viewModel.state.value.profilePic != null) {
            rememberImagePainter(viewModel.state.value.profilePic)
        } else {
            painterResource(id = R.drawable.empty_profile_pic)
        }
        Image(
            painter = painter,
            contentDescription = "profile icon",
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .size(dimensionResource(id = R.dimen.dimen_60))
                .clip(CircleShape)
                .fillMaxSize()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

    }
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}

@Composable
private fun DoBackPress(context: Context) {
    var showToast by remember { mutableStateOf(false) }
    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    if (showToast) {
        Toast.makeText(context, "Press BACK again to exit", Toast.LENGTH_SHORT).show()
        showToast = false

    }
    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }
}


//@Composable
//fun AutoScrollingCardList(cards: List<CardItem>) {
//    val currentPage by remember { mutableStateOf(0) }
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        LazyRow(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            items(cards) { cardItem ->
//                AutoScrollingCard(cardItem = cardItem)
//            }
//        }
//
//        DotIndicator(
//            itemCount = cards.size,
//            currentPage = currentPage,
//            modifier = Modifier
//                .padding(top = 8.dp)
//        )
//    }
//}

//@Composable
//fun AutoScrollingCardList(cards: List<CardItem>) {
//    val scrollState = rememberLazyListState()
//    var currentItem by remember { mutableStateOf(0) }
//
//    LaunchedEffect(scrollState, currentItem) {
//        while (cards.isNotEmpty()) {
//            delay(2000) // Adjust the delay as needed
//            currentItem = (currentItem + 1) % cards.size
//            scrollState.animateScrollToItem(currentItem)
//        }
//    }
//
//    LazyRow(state = scrollState) {
//        items(cards.size) { index ->
//            AutoScrollingCard(cardItem = cards[index])
//        }
//    }
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoScrollingCard(cardItem:CardItem) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp)
            .padding(5.dp)
            .clip(MaterialTheme.shapes.medium),
        onClick = { /* Handle card click */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape)
//                    .background(ColorPrimary)
//                    .padding(8.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
            Text(text = cardItem.title, style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cardItem.subtitle, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun DotIndicator(
    itemCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(itemCount) { index ->
            Dot(isSelected = index == currentPage)
        }
    }
}

@Composable
fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(
                color = if (isSelected) ColorPrimary
                else ColorPrimary.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .padding(4.dp)
    )
}

data class CardItem(val title: String, val subtitle: String)

//@Preview(showBackground = true)
//@Composable
//fun AutoScrollingCardListPreview() {
//    val cardItems = List(5) {
//        CardItem("Title $it", "Subtitle $it")
//    }
//    AutoScrollingCardList(cards = cardItems)
//}

@Composable
fun LazyListItem(text: String) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .size(150.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material.Text(
            text = text,
            fontSize = 18.sp
        )
    }
}