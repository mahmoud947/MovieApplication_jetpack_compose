package com.example.movieapplicationjetpackcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.movieapplicationjetpackcompose.R

val mulishFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = com.example.domain.R.font.mulish,
            weight = FontWeight.W400,
            style = FontStyle.Italic
        ),
        Font(
            resId = com.example.domain.R.font.mulish_bold,
            weight = FontWeight.W600,
            style = FontStyle.Normal
        ),
    )
)
val merriweatherFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = com.example.domain.R.font.merriweather_black,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),

        )
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)