package com.gjglobal.daily_task_entry.presentation.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.presentation.theme.BlueRms
import com.gjglobal.daily_task_entry.presentation.utils.Screen


@Composable
fun SplashScreen(
    navController: NavController,
    activity: Activity,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cacheManager = CacheManager(context)
    val state = viewModel.state.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueRms),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.padding(top = 10.dp),
                painter = painterResource(id = R.drawable.notes),
                contentDescription = "product logo"
            )

        }
        if (state.isLoading.not()) {
            LaunchedEffect(key1 = true) {

                try {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.SplashScreen.route) {
                                inclusive = true
                            }
                        }


                }catch (e:Exception){
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }

                }
            }
        }
    }
}