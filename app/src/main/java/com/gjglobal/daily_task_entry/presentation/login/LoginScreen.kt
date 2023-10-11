package com.gjglobal.daily_task_entry.presentation.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.presentation.components.DialogPopup
import com.gjglobal.daily_task_entry.presentation.components.LottiePopUp
import com.gjglobal.daily_task_entry.presentation.components.LottiePopUpPlay
import com.gjglobal.daily_task_entry.presentation.components.OnLifeCycleEvent
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardActivity
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.LightBlue
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_24
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_24
import com.gjglobal.daily_task_entry.presentation.theme.blueVarient


@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(
    navController: NavController,
    activity: Activity,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = viewModel.state.value
    val context = LocalContext.current
    val focusRequesterForPassword by remember { mutableStateOf(FocusRequester()) }
    val cacheManager = CacheManager(context)

    OnLifeCycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                try {
                    if (cacheManager.getUsernamePassword()?.username!!.isNotEmpty()) {
                        viewModel.login(
                            cacheManager.getUsernamePassword()?.username!!,
                            cacheManager.getUsernamePassword()?.password!!,
                            activity
                        )
                    }

                } catch (e: Exception) {
                    println(e)
                }


            }
            Lifecycle.Event.ON_STOP -> {

            }
            else -> {

            }
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(ColorPrimary),
        ) {

        }
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
//                Image(
//                    modifier = Modifier.padding(top = 100.dp),
//                    painter = painterResource(id = R.drawable.notes),
//                    contentDescription = "Task logo"
//                )

                LottiePopUpPlay(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_250)),
                    lottie = R.raw.task2
                )

                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    "GJ Task Management",
                    style = TextStyle_600_24,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(100.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(vertical = 20.dp, horizontal = 35.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Welcome!",
                                color = TextColor,
                                style = TextStyle_500_24,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Sign in to your account",
                                color = TextColor,
                                textAlign = TextAlign.Left,
                                style = TextStyle_400_14,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        OutlinedTextField(
                            value = state.userName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start)
                                .focusRequester(focusRequester),
                            onValueChange = {
                                viewModel.isValidUsername(it)
                            },
                            textStyle = TextStyle_500_16,
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                                onNext = { focusRequesterForPassword.requestFocus() }),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            singleLine = true,
                            label = { Text(text = "UserName") },
                            placeholder = { Text(text = "Enter your UserName") },
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        val showPassword = remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = state.password,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start)
                                .focusRequester(focusRequesterForPassword),
                            onValueChange = {
                                viewModel.isValidPassword(it)
                            },
                            textStyle = TextStyle_500_16,
                            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            singleLine = true,
                            label = { Text(text = "Password") },
                            placeholder = { Text(text = "Enter your Password") },
                            trailingIcon = {
                                val image = if (showPassword.value) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff
                                if (showPassword.value) stringResource(id = R.string.hide_password) else stringResource(
                                    id = R.string.show_password
                                )
                                Image(imageVector = image,
                                    contentDescription = stringResource(id = R.string.location),
                                    modifier = Modifier
                                        .padding(
                                            horizontal = dimensionResource(id = R.dimen.dimen_14),
                                            vertical = dimensionResource(id = R.dimen.dimen_8)
                                        )
                                        .clickable {
                                            showPassword.value = !showPassword.value
                                        },
                                    colorFilter = ColorFilter.tint(blueVarient))
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Forgot Password?",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    },
                                textAlign = TextAlign.End,
                                style = TextStyle_500_12,
                                color = LightBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                   focusManager.clearFocus()
                                    viewModel.login(state.userName, state.password,activity)

                            },
                            colors = ButtonDefaults.buttonColors(ColorPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            if (state.isLoading.not()) {
                                Text(
                                    text = "SIGN IN",
                                    color = Color.White,
                                    style = TextStyle_500_14
                                )
                            }
                            AnimatedVisibility(visible = state.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(180.dp))
                    }
                }
           }
        }
    }
if(state.isLogout){
    DialogPopup(
        activity = activity,
        onClickCancelBtn = {
            state.isLogout = false
        },
        onOkBtn = {
            //logoutViewModel.logout(activity)
        },
        message = "Your logout failed last time \n going to logout now",
        logoutType = "already_login"
    )
}
    if (state.isLoading.not()) {
        if (state.authorization != null) {
            Log.e("LOGIN DATA", state.authorization.toString())
            LaunchedEffect(key1 = true) {

                activity.startActivity(
                    Intent(
                        activity,
                        DashboardActivity::class.java
                    )
                )
                activity.finish()

            }

        }
    } else if (state.error.isNullOrEmpty().not()) {
        if (state.authorization?.message.isNullOrEmpty().not()) {
            LaunchedEffect(key1 = true) {
                Toast.makeText(activity, state.authorization!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
