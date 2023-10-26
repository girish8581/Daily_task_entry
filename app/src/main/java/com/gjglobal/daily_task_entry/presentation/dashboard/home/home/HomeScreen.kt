package com.gjglobal.daily_task_entry.presentation.dashboard.home.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.core.Constants
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.domain.domain.model.task.taskcount.taskCountSummaryRequest
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BlueWhite
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DarkGreen
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.PurpleGrey80
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_20
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import java.io.File

@Composable
fun HomeScreenOld(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel,
    viewModel: HomeScreenViewModel= hiltViewModel()
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
        ), label = ""
    )

//    if (animatedOffsetX >= 300f || animatedOffsetX <= -300f) {
//        direction *= -1
//    }

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
            .background(ColorPrimary)
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
                                text = staffName!!, style = TextStyle_800_18,color = White
                            )
                            Text(
                                text = designation!!, style = TextStyle_400_12,color = White
                            )
                        }

                    }
                }
            }

            Text(
                text = "My Tasks", style = TextStyle_800_20, color = White,modifier = Modifier.padding(horizontal = 20.dp)
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
                        .background(White)
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
                            painter = painterResource(id = R.drawable.todo_icon),
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


                            if (state.taskCount?.get(0)?.todoCount.isNullOrBlank().not()){
                            if (state.isTaskCount!!) {
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
                LaunchedEffect(key1 = direction) {
                    offsetX += direction * 100f // Adjust the speed of movement
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
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
                        painter = painterResource(id = R.drawable.inprogress),
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

                        if(state.taskCount?.get(0)?.inProgressCount.isNullOrBlank().not()) {
                        if(state.isTaskCount!!) {
                            Text(
                                text = if (state.isTaskCount!!) {
                                    state.taskCount?.get(0)?.inProgressCount!! + " Tasks"
                                } else {
                                    "0 Tasks"
                                }, style = TextStyle_600_12,
                                color = if (state.taskCount?.get(0)?.inProgressCount!!.toInt() != 0) {
                                    Color.Red
                                } else {
                                    DarkGreen
                                },
                                modifier = Modifier
                                    .offset(
                                        x = if (state.taskCount?.get(0)?.inProgressCount!!.toInt() != 0) {
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
                        .background(PurpleGrey80)
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
                            painter = painterResource(id = R.drawable.qa_icon),
                            contentDescription = "profile icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "Under QA", style = TextStyle_800_18
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
                            painter = painterResource(id = R.drawable.done_icon),
                            contentDescription = "profile icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "QA Passed", style = TextStyle_800_18
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
                            painter = painterResource(id = R.drawable.leave),
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
fun BoxRow(
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
fun ProfileImagePicker(
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
                color = Color.Green,
                modifier = Modifier
                    .size(30.dp)
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
                            id = R.dimen.dimen_15
                        ),
                        horizontal = dimensionResource(id = R.dimen.dimen_20)
                    )
                    .size(dimensionResource(id = R.dimen.dimen_50))
                    .clip(CircleShape)
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
    }

