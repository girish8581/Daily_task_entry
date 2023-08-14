package com.gjglobal.daily_task_entry.presentation.dashboard.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.components.Messagebox
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.BlueWhite
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_20
import com.gjglobal.daily_task_entry.presentation.theme.doneColor
import com.gjglobal.daily_task_entry.presentation.theme.inProgressColor
import com.gjglobal.daily_task_entry.presentation.theme.todoColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    activity: Activity,
    dashViewModel: DashboardViewModel
) {



    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                //appointmentViewModel.getSpecialityWiseDoctorsList()
                dashViewModel.hideBottomMenu(false)
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
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                            .fillMaxWidth()
                            .graphicsLayer(shadowElevation = 5f) // Add a shadow for visual depth

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_icons),
                            contentDescription = "profile icon",
                            modifier = Modifier.size(90.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "Shyama Prabha", style = TextStyle_800_18
                            )
                            Text(
                                text = "Android Developer", style = TextStyle_400_12
                            )
                        }

                    }
                }
            }

            Text(
                text = "My Tasks", style = TextStyle_800_20, modifier = Modifier.padding(20.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    ),
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
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )

                            .fillMaxWidth()


                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier.size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "To do", style = TextStyle_800_18
                            )


                            Text(
                                text = "0 Tasks", style = TextStyle_400_12
                            )
                        }
                    }
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
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    )
                        .fillMaxWidth()


                ) {
                    Image(
                        painter = painterResource(id = R.drawable.todo),
                        contentDescription = "profile icon",
                        modifier = Modifier.size(60.dp)
                            .padding(10.dp)
                    )


                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp)

                    ) {

                        Text(
                            text = "In Progress", style = TextStyle_800_18
                        )


                        Text(
                            text = " 2 Tasks", style = TextStyle_400_12
                        )
                    }
                }
            }
        }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp, horizontal = 20.dp
                    ),
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
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )

                            .fillMaxWidth()


                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier.size(60.dp)
                                .padding(10.dp)
                        )


                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp)

                        ) {

                            Text(
                                text = "Done", style = TextStyle_800_18
                            )


                            Text(
                                text = "5 Tasks", style = TextStyle_400_12
                            )
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
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )

                            .fillMaxWidth()


                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.todo),
                            contentDescription = "profile icon",
                            modifier = Modifier.size(60.dp)
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






