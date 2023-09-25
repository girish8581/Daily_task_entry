package com.gjglobal.daily_task_entry.presentation.dashboard

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.components.NoInternetScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.HomeScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.home.leave.LeaveScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.tasklist.TaskListScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.taskview.TaskListViewScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.more.MoreScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.ReportScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.more.task.TaskScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.more.taskassign.TaskAssignScreen
import com.gjglobal.daily_task_entry.presentation.dashboard.notification.NotificationScreen
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.DailyActivityApplicationTheme
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.utils.Screen
import com.gjglobal.daily_task_entry.presentation.utils.network.ConnectivityObserver
import com.gjglobal.daily_task_entry.presentation.utils.network.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DashboardActivity : ComponentActivity() {
    val viewModel: DashboardViewModel by viewModels()
    private lateinit var connectivityObserver: ConnectivityObserver

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        setContent {
            DailyActivityApplicationTheme {
                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Available
                )
                if (status == ConnectivityObserver.Status.Available) {
                    val navController = rememberNavController()
                    Scaffold(
                        backgroundColor = Color.White,
                        bottomBar = {
                            if (viewModel.state.value.hideBottomMenu.not()) {
                                BottomNavigationBar(
                                    items = listOf(
                                        BottomNavItem(
                                            name = "Home",
                                            route = Screen.HomeScreen.route,
                                            icon = painterResource(id = R.drawable.home)
                                        ),
                                        BottomNavItem(
                                            name = "Report",
                                            route = Screen.ReportScreen.route,
                                            icon = painterResource(id = R.drawable.report)
                                        ),
                                        BottomNavItem(
                                            name = "More",
                                            route = Screen.MoreScreen.route,
                                            icon = painterResource(id = R.drawable.more)
                                        ),
                                    ),
                                    navController = navController,
                                    onItemClick = {
                                        if (navController.currentBackStackEntry?.destination?.route != it.route) {
                                            navController.navigate(it.route)
                                        }
                                    }
                                )
                            }
                        }
                    ) { padding ->
                        Navigation(
                            navController = navController,
                            modifier = Modifier
                                .padding(padding),
                            activity = this@DashboardActivity,
                            viewModel = viewModel
                        )
                    }
                } else {
                    NoInternetScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier,
    activity: DashboardActivity,
    viewModel: DashboardViewModel
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController = navController, activity = activity, dashViewModel = viewModel)
        }
        composable(
            route = Screen.NotificationScreen.route
        ) {
            NotificationScreen(navController = navController)
        }
        composable(
            route = Screen.MoreScreen.route
        ) {
            MoreScreen(navController = navController,activity)
        }

        composable(
            route = Screen.TaskListScreen.route
        ) {
            TaskListScreen(navController = navController,activity = activity, dashViewModel = viewModel)
        }

        composable(
            route = Screen.LeaveScreen.route
        ) {
            LeaveScreen(navController = navController,activity = activity, dashViewModel = viewModel)
        }

        composable(
            route = Screen.TaskListCompleteViewScreen.route
        ) {
            TaskListViewScreen(navController = navController,activity = activity, dashViewModel = viewModel, taskStatus = "COMPLETED")
        }

        composable(
            route = Screen.TaskListToDoViewScreen.route
        ) {
            TaskListViewScreen(navController = navController,activity = activity, dashViewModel = viewModel, taskStatus = "TO DO")
        }

        composable(
            route = Screen.TaskScreen.route
        ) {
            TaskScreen(navController = navController,activity = activity, dashViewModel = viewModel)
        }

        composable(
            route = Screen.TaskAssignScreen.route
        ) {
            TaskAssignScreen(navController = navController,activity = activity, dashViewModel = viewModel)
        }

        composable(
            route = Screen.ReportScreen.route
        ) {
            ReportScreen(navController = navController,activity = activity, dashViewModel = viewModel)
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier.background(color = Color.White),
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                modifier = modifier.background(color = Color.White),
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = ColorPrimary,
                unselectedContentColor =TextColor ,
                icon = {
                    Column(
                        modifier = modifier.background(color = Color.White),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.name,
                            modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_25)),
                            tint = if (selected) {
                                ColorPrimary
                            } else {
                                TextColor
                            }
                        )
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_2)))
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                            color = if (selected) {
                                ColorPrimary
                            } else {
                                TextColor
                            }
                        )
                    }
                }
            )
        }
    }
}