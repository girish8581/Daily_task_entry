package com.gjglobal.daily_task_entry.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gjglobal.daily_task_entry.R


val Montserrat: FontFamily = FontFamily(
    Font(resId = R.font.montserrat_100, weight = FontWeight.W100),
    Font(resId = R.font.montserrat_200, weight = FontWeight.W200),
    Font(resId = R.font.montserrat_300, weight = FontWeight.W300),
    Font(resId = R.font.montserrat_400, weight = FontWeight.W400),
    Font(resId = R.font.montserrat_500, weight = FontWeight.W500),
    Font(resId = R.font.montserrat_600, weight = FontWeight.W600),
    Font(resId = R.font.montserrat_700, weight = FontWeight.W700),
    Font(resId = R.font.montserrat_800, weight = FontWeight.W800),
    Font(resId = R.font.montserrat_900, weight = FontWeight.W900)
)

val Courier: FontFamily = FontFamily(
    Font(resId = R.font.courier_bold, weight = FontWeight.W500),
)

val TypographyCourier = Typography(
    defaultFontFamily = Courier,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
)

val TextStyle_100_Courier = TextStyle(
    fontFamily = Courier,
    fontWeight = FontWeight.W500,
    color = Color.Black
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = Montserrat,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

val TextStyle_100 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W100,
    color = TextColor
)

val TextStyle_200 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W200,
    color = TextColor
)

val TextStyle_300 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W300,
    color = TextColor
)

val TextStyle_400 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W400,
    color = TextColor
)

val TextStyle_500 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W500,
    color = TextColor
)

val TextStyle_600 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W600,
    color = TextColor
)

val TextStyle_700 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W700,
    color = TextColor
)

val TextStyle_800 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W800,
    color = TextColor
)

val TextStyle_900 = TextStyle(
    fontFamily = Montserrat,
    fontWeight = FontWeight.W900,
    color = TextColor
)