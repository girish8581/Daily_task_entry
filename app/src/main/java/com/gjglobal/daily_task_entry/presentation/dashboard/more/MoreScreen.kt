package com.gjglobal.daily_task_entry.presentation.dashboard.more

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.presentation.components.DialogPopup
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.utils.MainActivity
import com.gjglobal.daily_task_entry.presentation.utils.Screen

@Composable
fun MoreScreen(navController: NavController, activity: Activity,viewModel: MoreViewModel = hiltViewModel()) {
    var showWarnLogout by remember { mutableStateOf(false) }
    val state = viewModel.state.value
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val userData = cacheManager.getAuthResponse()?.data?.get(0)
    val userRole = userData?.userType

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            ) {

                ToolBar(nameOfScreen = "More", iconOfScreen = 0, onClick = {
                    navController.popBackStack()
                }, onIconClick = {})
            }
            if (userRole == "ADMIN"){
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                navController.navigate(Screen.TaskScreen.route)
                            }
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painterResource(id = R.drawable.logoutlogo),
                            contentDescription = "add task icon",
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Add Task", style = TextStyle_400_14
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painterResource(id = R.drawable.forwardarrow),
                            modifier = Modifier.padding(end = 20.dp),
                            contentDescription = "back arrow ",
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                navController.navigate(Screen.TaskAssignScreen.route)
                            }
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painterResource(id = R.drawable.logoutlogo),
                            contentDescription = "task map icon",
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Assign Task", style = TextStyle_400_14
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painterResource(id = R.drawable.forwardarrow),
                            modifier = Modifier.padding(end = 20.dp),
                            contentDescription = "back arrow ",
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            navController.navigate(Screen.ReportScreen.route)

                        }
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painterResource(id = R.drawable.logoutlogo),
                        contentDescription = "logout icon",
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Reports", style = TextStyle_400_14
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painterResource(id = R.drawable.forwardarrow),
                        modifier = Modifier.padding(end = 20.dp),
                        contentDescription = "back arrow ",
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            showWarnLogout = true
                        }
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painterResource(id = R.drawable.logoutlogo),
                        contentDescription = "logout icon",
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Logout", style = TextStyle_400_14
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painterResource(id = R.drawable.forwardarrow),
                        modifier = Modifier.padding(end = 20.dp),
                        contentDescription = "back arrow ",
                    )
                }
            }
            }
        }


        if (showWarnLogout) {
            DialogPopup(
                activity = activity,
                onClickCancelBtn = {
                    showWarnLogout = false
                },
                onOkBtn = {
                    activity.startActivity(
                        Intent(
                            activity,
                            MainActivity::class.java
                        )
                    )
                    activity.finish()
                    // viewModel.logout(activity)
                },
                message = "Do you want to Logout Task App?",
                logoutType = "normal_logout"
            )
        }
    }
