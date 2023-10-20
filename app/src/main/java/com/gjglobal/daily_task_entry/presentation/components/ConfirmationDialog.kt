package com.gjglobal.daily_task_entry.presentation.components

import android.app.Activity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gjglobal.daily_task_entry.presentation.theme.BgBlur
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.darkred


@Composable
fun DialogPopup(
    message: String,activity: Activity,onClickCancelBtn:()-> Unit,onOkBtn:()->Unit,logoutType:String
) {

    Box(
        Modifier
            .fillMaxSize()
            .background(BgBlur)
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(all =16.dp),
            shape = RoundedCornerShape(
                20.dp
            )
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Logout",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text =   message ,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White
                        )
                ) {  if(logoutType == "normal_logout"){
                    Button(
                        onClick = {
                            onClickCancelBtn.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(darkred),
                        modifier = Modifier
                            .weight(0.3f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {

                        Text(
                            text = "No",
                            color = Color.White,
                            // style = Eina01_Semi_Bold_14
                        )

                    }
                    Spacer(modifier = Modifier.size(30.dp))
                }

                    Button(
                        onClick = {
                            onOkBtn.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(ColorPrimary),
                        modifier = Modifier
                            .weight(0.3f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = "Yes",
                            color = Color.White,
                            //  style = Eina01_Semi_Bold_14
                        )
                    }
                }

            }
        }
    }
}
