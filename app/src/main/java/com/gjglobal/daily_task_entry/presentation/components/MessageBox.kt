package com.gjglobal.daily_task_entry.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_600_14
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun Messagebox(onSuccess: () -> Unit,message:String) {
    val isDialogVisible = remember { mutableStateOf(true) }
    if (isDialogVisible.value) {
        Card(
            modifier = Modifier
                .padding(horizontal = (dimensionResource(id = R.dimen.dimen_30)))
                .padding(top = (dimensionResource(id = R.dimen.dimen_250))),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_20))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_5))
            ) {

                LottiePopUp(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_150)),
                    lottie = R.raw.message_success
                )
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TextStyle_600_14
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_10)))
            }
        }
        LaunchedEffect(Unit) {
            delay(2.seconds)
            onSuccess.invoke()
            isDialogVisible.value = false

        }

    }
}

@Composable
fun LottiePopUp(modifier: Modifier, lottie: Int) {
    val isPlaying by remember {
        mutableStateOf(true)
    }
    val speed by remember {
        mutableStateOf(1f)
    }
    val composition by rememberLottieComposition(

        LottieCompositionSpec.RawRes(lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false,
    )
    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
    )
}

@Composable
fun LottiePopUpPlay(modifier: Modifier, lottie: Int) {
    var isPlaying by remember { mutableStateOf(true) }
    val speed by remember { mutableStateOf(1f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottie))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        speed = speed,
    )

    if (progress == 1f && isPlaying) {
        // Animation has reached the end, toggle isPlaying to restart the animation
        isPlaying = false
    } else if (progress == 0f && !isPlaying) {
        // Animation has restarted, toggle isPlaying to play it
        isPlaying = true
    }

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier,
    )
}
