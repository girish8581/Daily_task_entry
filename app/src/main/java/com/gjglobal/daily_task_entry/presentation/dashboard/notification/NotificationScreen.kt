package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import ShimmerEffectListView
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.notification.NotificationItem
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.components.showNotification
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BlueRms
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_10
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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



    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                if(userData?.userType=="ADMIN"){
                    viewModel.getNotification()
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


    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToolBar(nameOfScreen = "Notification",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})

            Spacer(modifier = Modifier.height(20.dp))

            if(viewModel.state.value.isNotificationList!!){
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
                                NotificationCard(items=items,item=item)
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
fun NotificationCard(items:List<NotificationItem>, item:Int){
    val maxLength = 40
    var notificationMore by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }


    val message = items[item].message
    //showNotification(context = LocalContext.current,message)
            var moreText = " View More"
    val nameShort = nameInitials(items[item].userName)


    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = dimensionResource(id = R.dimen.dimen_10),
            horizontal = dimensionResource(id = R.dimen.dimen_20)
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_20)),
        elevation = 5.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {

            Row(
                modifier = Modifier.
                fillMaxWidth()
                    .padding(start=10.dp,end=10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(all = dimensionResource(id = R.dimen.dimen_10))
                        .size(dimensionResource(id = R.dimen.dimen_50))
                        .background(BlueRms, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(nameShort, color = Color.White)
                }
//
//                val truncatedMessage = if (message.length > maxLength) {
//                    message.take(maxLength) + "..." // Truncate the string if it's longer than maxLength
//                } else {
//                    message
//                }
//
//                msg = truncatedMessage
//
////        val annotatedString = buildAnnotatedString {
////            withStyle(style = SpanStyle(color = TextColor)) {
////                append(truncatedMessage)
////            }
////            withStyle(
////                style = SpanStyle(
////                    color = BlueRms,
////                    textDecoration = TextDecoration.Underline
////                )
////            ) {
////                append(" More")
////            }
////        }
////        Text(
////            text = annotatedString,
////            style = TextStyle_400_16,
////            modifier = Modifier.padding(top = 10.dp)
////                .width(150.dp)
////        )
//
//
////            val message = items[item].message
////            val moreText = " More"
////
//                // Combine the message and "More" text with different styles
//                val annotatedString = buildAnnotatedString {
//                    withStyle(style = SpanStyle(color = TextColor)) {
//                        append(msg)
//                    }
//                    pushStringAnnotation(tag = "More", annotation = moreText)
//                    withStyle(
//                        style = SpanStyle(
//                            color = BlueRms,
//                            textDecoration = TextDecoration.Underline
//                        )
//                    ) {
//                        append(moreText)
//                    }
//                    pop()
//                }
//
//                ClickableText(
//                    text = annotatedString,
//                    onClick = { offset ->
//                        val annotation = annotatedString.getStringAnnotations(
//                            tag = "More",
//                            start = offset,
//                            end = offset
//                        ).firstOrNull()
//
//                        if (annotation != null) {
//                            moreText = if(notificationMore){
//                                " View More"
//                            }else {
//                                " View Less"
//
//                            }
//
//                            msg = if(notificationMore){
//                                ""
//                            }else {
//                                ""
//
//                            }
//                            notificationMore = !notificationMore
//                            // Handle the "More" click action here
//                            // You can show a dialog or navigate to a different screen, for example.
//                        }
//                    },
//                    style = TextStyle_500_12,
//                    modifier = Modifier.padding(top = 10.dp)
//                )

                if (!notificationMore) {
                    Text(
                        text = items[item].message, color = TextColor,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        style = TextStyle_500_12,
                        modifier = Modifier.width(250.dp)
                            .padding(start = 10.dp,top=10.dp)
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