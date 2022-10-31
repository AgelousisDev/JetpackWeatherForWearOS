package com.agelousis.jetpackweatherwearos.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import com.agelousis.jetpackweatherwearos.R

private val fonts = FontFamily(
    Font(R.font.ubuntu_regular),
    Font(R.font.ubuntu_bold, weight = FontWeight.Bold),
    Font(R.font.ubuntu_light, weight = FontWeight.Light),
    Font(R.font.ubuntu_light, weight = FontWeight.Thin),
    Font(R.font.ubuntu, weight = FontWeight.Normal, style = FontStyle.Italic)
)

val textViewAlertTitleFont = TextStyle(
    fontFamily = FontFamily(Font(R.font.ubuntu_regular)),
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp
)

val textViewHeaderFont = TextStyle(
    fontFamily = fonts,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp
)

val TextStyle.medium
    get() = merge(
        other = TextStyle(
            fontWeight = FontWeight.Medium
        )
    )

val TextStyle.bold
    get() = merge(
        other = TextStyle(
            fontWeight = FontWeight.Bold
        )
    )

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    display1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    body2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
    display2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp
    ),
    display3 = TextStyle(
        fontFamily = fonts,
        fontStyle = FontStyle.Normal,
        fontSize = 14.sp
    ),
    caption1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize= 14.sp
    ),
    caption2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize= 12.sp
    ),
    caption3 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize= 10.sp
    )
)