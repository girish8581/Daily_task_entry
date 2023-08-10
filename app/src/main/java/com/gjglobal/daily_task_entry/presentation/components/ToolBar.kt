package com.gjglobal.daily_task_entry.presentation.components



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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.theme.TextDefColor
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_14
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_16


@Composable
fun ToolBar(nameOfScreen:String,iconOfScreen:Int,onClick:(() -> Unit),onIconClick:(() -> Unit)){
    val borderThickness = 1.dp
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)) {
        Column(modifier = Modifier
            .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Card(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(start = 10.dp, top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Image(
                        painterResource(id = R.drawable.backarrow),
                        contentDescription = "backArrow",
                        modifier = Modifier.clickable {
                            onClick.invoke()
                        }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    androidx.compose.material3.Text(
                        text = nameOfScreen, color = TextDefColor,
                        style = TextStyle_500_16,
                        modifier = Modifier.clickable {
                            onClick.invoke()
                        }
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(180.dp))

                    if (iconOfScreen != 0){
                        Image(
                            painterResource(iconOfScreen),
                            contentDescription = "callendarIcon",
                            modifier = Modifier
                                .clickable {
                                    onIconClick.invoke()
                                }
                                .width(16.dp)
                                .height(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ToolbarSearch(searchText: String, onChange: ((String) -> Unit), focusManager: FocusManager) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = colorResource(id = R.color.white))
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_20)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val borderThickness = 1.dp
            BasicTextField(
                value = searchText,
                onValueChange = onChange,
                singleLine = true,
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_5))
                    )
//                    .border(
//                        borderThickness,
//                        color = colorResource(id = R.color.radiobutton_def)
//                    ) // Set the border color here
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_20))
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        bottom = dimensionResource(id = R.dimen.dimen_5)
                    ),
                enabled = true, readOnly = false, textStyle = TextStyle_400_14,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ), decorationBox = { innerTextField ->
                    Row(horizontalArrangement = Arrangement.Start) {
                        Image(
                        painterResource(id = R.drawable.searchicon),
                            contentDescription = "searchIcon",
                            modifier = Modifier.padding(top = 3.dp))
                        innerTextField()
                    }
                }
            )
        }
}
