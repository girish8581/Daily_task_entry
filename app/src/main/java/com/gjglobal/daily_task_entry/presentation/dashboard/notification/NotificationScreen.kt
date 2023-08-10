package com.gjglobal.daily_task_entry.presentation.dashboard.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.theme.BlueRms
import com.gjglobal.daily_task_entry.presentation.theme.TextColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_16
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_14


@Composable
fun NotificationScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ToolBar(nameOfScreen = "Notification",iconOfScreen = 0, onClick = {
                navController.popBackStack() }, onIconClick = {})

            Spacer(modifier = Modifier.width(20.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                items(0
//                    items
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                           // val nameShort = nameInitials(item.itemName)
                            Box(
                                modifier = Modifier
                                    .padding(all = dimensionResource(id = R.dimen.dimen_10))
                                    .size(dimensionResource(id = R.dimen.dimen_50))
                                    .background(BlueRms, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                               // Text(nameShort, color = Color.White)
                            }


                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {


                                    val annotatedString = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = TextColor)) {
                                           // append(item.itemDetails)
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                color = BlueRms,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append(" More")
                                        }
                                    }
                                    Text(
                                        text = annotatedString,
                                        style = TextStyle_400_16,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )


                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text ="",
                                  //  item.itemTime,
                                    color = TextColor,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = true,
                                    style = TextStyle_500_14
                                )
                            }
                        }

                    }
                }
            }
        }
        }
    }


data class NotificationItem(
    val itemName: String,
    val itemDetails: String,
    val itemTime: String
)
