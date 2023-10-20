package com.gjglobal.daily_task_entry.presentation.dashboard.home.home.adminhome

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.core.Constants
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.HomeScreenViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_20
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import java.io.File

@Composable
fun AdminHomeScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val staffName = userData?.staff_name
    val designation = userData?.designation

    var offsetX by remember { mutableStateOf(0f) }
    val direction by remember { mutableStateOf(1f) }
    val id = userData?.ImageId
    //Profile image loading url

    val url  = Constants.BASE_URL+"/services/image_api.php?id=$id"
    val profileImageUrl by remember { mutableStateOf(url) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    var profileImage: File? by remember { mutableStateOf(null) }

    val state = viewModel.state.value

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.gatTasksCount(taskCountSummaryRequest(
                    staff_name = staffName!!
                ))
                dashViewModel.hideBottomMenu(false)
                viewModel.downloadProfilePicture(userData.ImageId.toInt(),activity)
            }

            Lifecycle.Event.ON_STOP -> {

                dashViewModel.hideBottomMenu(false)
            }

            else -> {

            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            )
                            .fillMaxWidth()
                            .graphicsLayer(shadowElevation = 5f) // Add a shadow for visual depth

                    ) {
                        ImageFromApi(viewModel)

                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = staffName!!, style = TextStyle_800_18
                            )
                            Text(
                                text = designation!!, style = TextStyle_400_12
                            )
                        }

                    }
                }
            }

            Text(
                text = "My Tasks", style = TextStyle_800_20, modifier = Modifier.padding(horizontal = 20.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    )
                    .clickable {
                        navController.navigate(Screen.TaskListToDoViewScreen.route)
                    },
                shape = RoundedCornerShape(20.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(todoColor)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            )

                            .fillMaxWidth()


                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "To do", style = TextStyle_800_18
                            )


                            if (state.taskCount?.get(0)?.todo_count.isNullOrBlank().not()){
                            if (state.isTaskCount!!) {
                                Text(
                                    text = if (state.isTaskCount!!) {
                                        state.taskCount?.get(0)?.todo_count!! + " Tasks"
                                    } else {
                                        "0 Tasks"
                                    }, style = TextStyle_600_12,
                                    color = if (state.taskCount?.get(0)?.todo_count!!.toInt() != 0) {
                                        Color.Red
                                    } else {
                                        DarkGreen
                                    },
                                    modifier = Modifier
                                        .offset(
                                            x = if (state.taskCount?.get(0)?.todo_count!!.toInt() != 0) {
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
                LaunchedEffect(key1 = direction) {
                    offsetX += direction * 100f // Adjust the speed of movement
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //navController.navigate(Screen.ForgotPasswordScreen.route)

                        navController.navigate(Screen.TaskListScreen.route)
                    }
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    ),
                shape = RoundedCornerShape(20.dp),
            ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(inProgressColor)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                        .fillMaxWidth()


                ) {
                    Image(
                        painter = painterResource(id = R.drawable.todo),
                        contentDescription = "profile icon",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp)
                    )


                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp)

                    ) {

                        Text(
                            text = "In Progress", style = TextStyle_800_18
                        )

                        if(state.taskCount?.get(0)?.in_progress_count.isNullOrBlank().not()) {
                        if(state.isTaskCount!!) {
                            Text(
                                text = if (state.isTaskCount!!) {
                                    state.taskCount?.get(0)?.in_progress_count!! + " Tasks"
                                } else {
                                    "0 Tasks"
                                }, style = TextStyle_600_12,
                                color = if (state.taskCount?.get(0)?.in_progress_count!!.toInt() != 0) {
                                    Color.Red
                                } else {
                                    DarkGreen
                                },
                                modifier = Modifier
                                    .offset(
                                        x = if (state.taskCount?.get(0)?.in_progress_count!!.toInt() != 0) {
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
        }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    )
                    .clickable {
                        navController.navigate(Screen.TaskListCompleteViewScreen.route)
                    },
                shape = RoundedCornerShape(20.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(doneColor)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            )

                            .fillMaxWidth()


                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "Done", style = TextStyle_800_18
                            )

                            if(state.taskCount?.get(0)?.completed_count.isNullOrBlank().not()) {
                                if (state.isTaskCount!!) {
                                    Text(
                                        text = if (state.isTaskCount!!) {
                                            state.taskCount?.get(0)?.completed_count!! + " Tasks"
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    )
                    .clickable {
                        navController.navigate(Screen.LeaveScreen.route)
                    },
                shape = RoundedCornerShape(20.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightBlue)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            )

                            .fillMaxWidth()


                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "Leave", style = TextStyle_800_18
                            )


                            Text(
                                text = "Update leave status", style = TextStyle_400_12
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageFromApi(viewModel: HomeScreenViewModel) {

    if(viewModel.state.value.isLoading){
        AnimatedVisibility(visible = true) {
            CircularProgressIndicator(
                color = Color.Green,
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }else{
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp
            )
        ) {
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
                    .padding(
                        vertical = dimensionResource(
                            id = R.dimen.dimen_10
                        )
                    )
                    .size(dimensionResource(id = R.dimen.dimen_70))
                    .clip(CircleShape)
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}

