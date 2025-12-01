package com.example.task3_2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.task3_2.R

// ---------- ШРИФТЫ ----------

private val RobotoMedium = FontFamily(
    Font(R.font.roboto_medium, weight = FontWeight.Medium)
)

private val RobotoSemiCondensedThin = FontFamily(
    Font(R.font.roboto_semicondensed_thin, weight = FontWeight.Thin)
)

// ---------- TYPOGRAPHY ----------

val CustomTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = RobotoMedium,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = RobotoSemiCondensedThin,
        fontWeight = FontWeight.Thin,
        fontSize = 20.sp
    ),
    displaySmall = TextStyle(
        fontFamily = RobotoMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
)

// ---------- COLOR SCHEMES ----------

private val DarkColorScheme = darkColorScheme(
    primary = onPrimaryLight,
    secondary = onSecondaryLight,
    tertiary = errorLight
)

private val LightColorScheme = lightColorScheme(
    primary = onPrimaryDark,
    secondary = onSecondaryDark,
    tertiary = errorDark
)

// ---------- THEME ----------

@Composable
fun Task3_2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content,
        shapes = shapes
    )
}
