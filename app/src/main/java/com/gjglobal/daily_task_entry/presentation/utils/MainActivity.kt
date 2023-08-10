package com.gjglobal.daily_task_entry.presentation.utils

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gjglobal.daily_task_entry.presentation.components.NoInternetScreen
import com.gjglobal.daily_task_entry.presentation.login.LoginScreen
import com.gjglobal.daily_task_entry.presentation.splash.SplashScreen
import com.gjglobal.daily_task_entry.presentation.theme.DailyActivityApplicationTheme
import com.gjglobal.daily_task_entry.presentation.utils.network.ConnectivityObserver
import com.gjglobal.daily_task_entry.presentation.utils.network.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    private lateinit var connectivityObserver: ConnectivityObserver
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            DailyActivityApplicationTheme {
                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Available
                )
                if (status == ConnectivityObserver.Status.Available) {
                    Surface(color = Color.Gray) {
                        val navController = rememberNavController()
                        val destination = Screen.SplashScreen.route
                        NavHost(
                            navController = navController,
                            startDestination = destination

                        ) {
                            composable(
                                route = Screen.SplashScreen.route
                            ) {
                                SplashScreen(navController, activity = this@MainActivity)
                            }
                            composable(
                                route = Screen.LoginScreen.route
                            ) {

                                LoginScreen(
                                    navController = navController,
                                    activity = this@MainActivity
                                )
                            }
                        }
                    }
                }else{
                    NoInternetScreen()
                }
            }
        }
    }

}
