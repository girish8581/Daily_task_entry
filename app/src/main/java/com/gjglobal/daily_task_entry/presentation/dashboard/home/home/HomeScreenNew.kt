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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
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
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import com.gjglobal.daily_task_entry.presentation.utils.currentDate
import java.io.File

@Composable
fun HomeScreen(
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = currentDate(), style = TextStyle_600_14, color = Gray,modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    ImageFromApi(viewModel)
                }

                Text(
                    text = "My Tasks", style = TextStyle_800_18, color = ColorPrimary,modifier = Modifier.padding(horizontal = 20.dp)
                )

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
                                if (state.taskCount?.get(0)?.todoCount.isNullOrBlank().not()) {
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
                                if (state.isTaskCount!!) {
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
                                    if (state.isTaskCount!!) {
                                        Text(
                                            text = if (state.isTaskCount!!) {
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
fun ImageFromApiNew(viewModel: HomeScreenViewModel) {

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

