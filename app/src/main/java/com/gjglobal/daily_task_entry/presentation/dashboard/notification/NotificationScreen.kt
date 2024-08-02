package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import ShimmerEffectListView
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationItem
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BlueRms
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(navController: NavController,
                       activity: Activity,
                       dashViewModel: DashboardViewModel,
                       viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val userName = userData?.staff_name
    val state = viewModel.state.value
    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                if(userData?.userType=="ADMIN"){
                    viewModel.getNotificationAdmin()
                }else{
                    viewModel.getNotification(userName!!)
                }

                viewModel.updatePrevCount(cacheManager=cacheManager)

                dashViewModel.hideBottomMenu(true)
            }

            Lifecycle.Event.ON_STOP -> {
                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }


    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToolBar(nameOfScreen = "Notification",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})

            Spacer(modifier = Modifier.height(20.dp))

            if(viewModel.state.value.isNotificationList){
                val items = viewModel.state.value.notificationList
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    items(
                        items?.size!!
                    ) { item ->
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            item {
                                NotificationCard(items=items,
                                    item=item,
                                    viewModel=viewModel,
                                    cacheManager=cacheManager)
                            }

                        }
                    }
                }
            }else{
                if(viewModel.state.value.isLoading){
                    ShimmerEffectListView(viewModel.state.value.isLoading)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationCard(items:List<NotificationItem>, item:Int,viewModel: NotificationViewModel,cacheManager: CacheManager){
    val maxLength = 40
    var notificationMore by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }

    val notificationReadStatus = viewModel.checkReadNotificationIndex(notificationIndex = items[item].id.toInt(),cacheManager=cacheManager)

    val message = items[item].message
    //showNotification(context = LocalContext.current,message)
            var moreText = " View More"
    val nameShort = nameInitials(items[item].userName)



   Card(
        modifier = Modifier.fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.dimen_10),
                horizontal = dimensionResource(id = R.dimen.dimen_20)
            )
            .clickable {
                if (!(viewModel.checkReadNotificationIndex(
                        notificationIndex = items[item].id.toInt(),
                        cacheManager = cacheManager
                    ))
                ) {
                    viewModel.saveNotificationReadStatus(
                        notificationIndex = items[item].id.toInt(),
                        cacheManager = cacheManager
                    )
                    viewModel.updateReadStatus()
                }
                Log.e("read list", viewModel.state.value.readNot.toString())
            },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_20)),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(if (notificationReadStatus) {
                    Color.White
                } else {
                    Color.LightGray
                }),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Create a separate Row for the Image and position it at the top
                if(!notificationReadStatus){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent), // Make the background transparent
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.successtickred),
                            contentDescription = "notifications",
                            modifier = Modifier.clickable {

                            }
                                .size(20.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(all = dimensionResource(id = R.dimen.dimen_10))
                        .size(dimensionResource(id = R.dimen.dimen_50))
                        .background(if (notificationReadStatus) {
                            Color.LightGray
                        } else {
                            BlueRms
                        }, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(nameShort, color = Color.White)
                }
                if (!notificationMore) {
                    Text(
                        text = items[item].message, color = if (notificationReadStatus) {
                            Color.DarkGray
                        } else {
                            TextColor
                        },
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        style = TextStyle_500_12,
                        modifier = Modifier.width(250.dp)
                            .padding(start = 10.dp, top = 10.dp)
                    )
                }
            }
            TimeDifferenceComposable(items[item].created_on)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeDifferenceComposable(time:String) {
    //val targetDateTime = "2023-10-14 02:37:29" // Replace with your target date and time
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val targetLocalDateTime = remember {
        LocalDateTime.parse(time, formatter)
    }

    val currentLocalDateTime = remember {
        LocalDateTime.now()
    }

    val duration = remember {
        Duration.between(currentLocalDateTime, targetLocalDateTime)
    }

    val daysDifference = duration.toDays()
    val hoursDifference = duration.toHours() % 24
    val minutesDifference = duration.toMinutes() % 60

    val displayText = if (daysDifference == 0L) {
        "Today"
    } else {
        "Time difference: $daysDifference days, $hoursDifference hours, $minutesDifference minutes"
    }

    Text(
        text = displayText,
        modifier = Modifier.padding(start=10.dp)
    )
}

fun nameInitials(name: String): String {
    val words = name.split(" ") // Split the name into words by space
    val initials = words.mapNotNull { it.firstOrNull() } // Extract the first character of each word
    return initials.joinToString("") // Join the initials to form a single string
}