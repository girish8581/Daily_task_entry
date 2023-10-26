package com.gjglobal.daily_task_entry.presentation.dashboard.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary

@Composable

fun MenuCard(){
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp),
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
            }

        }
    }
}



