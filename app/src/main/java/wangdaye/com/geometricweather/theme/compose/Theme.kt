package nowsci.com.temperateweather.theme.compose

import android.os.Build
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import nowsci.com.temperateweather.theme.compose.day.LightThemeColors
import nowsci.com.temperateweather.theme.compose.day.dynamicLightColors
import nowsci.com.temperateweather.theme.compose.night.DarkThemeColors
import nowsci.com.temperateweather.theme.compose.night.dynamicDarkColors

class TemperateWeatherColors {

    companion object {
        val LightPrimary1 = Color(0xffcfebf0)
        val LightPrimary2 = Color(0xffb6e3e7)
        val LightPrimary3 = Color(0xff96d6db)
        val LightPrimary4 = Color(0xff7ac7d3)
        val LightPrimary5 = Color(0xff75becb)

        val DarkPrimary1 = Color(0xff4b5073)
        val DarkPrimary2 = Color(0xff343851)
        val DarkPrimary3 = Color(0xff2c2f43)
        val DarkPrimary4 = Color(0xff20222f)
        val DarkPrimary5 = Color(0xff1a1b22)

        val DarkText = Color(0xff000000)
        val DarkText2nd = Color(0xff666666)
        val GreyText = Color(0xff4d4d4d)
        val GreyText2nd = Color(0xffb2b2b2)
        val LightText = Color(0xffffffff)
        val LightText2nd = Color(0xff999999)

        val Level1 = Color(0xff72d572)
        val Level2 = Color(0xffffca28)
        val Level3 = Color(0xffffa726)
        val Level4 = Color(0xffe52f35)
        val Level5 = Color(0xff99004c)
        val Level6 = Color(0xff7e0023)

        val WeatherSourceACCU = Color(0xffef5823)
        val WeatherSourceCN = Color(0xff033566)
        val WeatherSourceCaiYun = Color(0xff5ebb8e)

        val LightTitleText = DarkText
        val DarkTitleText = LightText

        val LightContentText = DarkText2nd
        val DarkContentText = LightText2nd

        val LightSubtitleText = GreyText2nd
        val DarkSubtitleText = GreyText
    }
}

private val DayColors = TemperateWeatherDayNightColors(
    titleColor = TemperateWeatherColors.LightTitleText,
    bodyColor = TemperateWeatherColors.LightContentText,
    captionColor = TemperateWeatherColors.LightSubtitleText,
    isDark = false,
)
private val NightColors = TemperateWeatherDayNightColors(
    titleColor = TemperateWeatherColors.DarkTitleText,
    bodyColor = TemperateWeatherColors.DarkContentText,
    captionColor = TemperateWeatherColors.DarkSubtitleText,
    isDark = true,
)

@Composable
fun TemperateWeatherTheme(
    lightTheme: Boolean,
    content: @Composable () -> Unit
) {
    val scheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && lightTheme ->
            dynamicLightColors(LocalContext.current)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !lightTheme ->
            dynamicDarkColors(LocalContext.current)
        lightTheme ->
            LightThemeColors
        else ->
            DarkThemeColors
    }
    val colors = if (lightTheme) DayColors else NightColors

    ProvideTemperateWeatherDayNightColors(colors = colors) {
        MaterialTheme(
            colorScheme = scheme,
            typography = TemperateWeatherTypography,
            content = content
        )
    }
}

@Composable
fun ProvideTemperateWeatherDayNightColors(
    colors: TemperateWeatherDayNightColors,
    content: @Composable () -> Unit
) {
    val value = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    value.update(colors)
    CompositionLocalProvider(
        LocalDayNightColors provides value,
        content = content
    )
}

object DayNightTheme {
    val colors: TemperateWeatherDayNightColors
        @Composable
        get() = LocalDayNightColors.current
}

private val LocalDayNightColors = staticCompositionLocalOf<TemperateWeatherDayNightColors> {
    error("No TemperateWeatherDayNightColors provided")
}

@Stable
class TemperateWeatherDayNightColors(
    titleColor: Color,
    bodyColor: Color,
    captionColor: Color,
    isDark: Boolean
) {
    var titleColor by mutableStateOf(titleColor)
        private set
    var bodyColor by mutableStateOf(bodyColor)
        private set
    var captionColor by mutableStateOf(captionColor)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: TemperateWeatherDayNightColors) {
        titleColor = other.titleColor
        bodyColor = other.bodyColor
        captionColor = other.captionColor
        isDark = other.isDark
    }

    fun copy(): TemperateWeatherDayNightColors = TemperateWeatherDayNightColors(
        titleColor = titleColor,
        bodyColor = bodyColor,
        captionColor = captionColor,
        isDark = isDark,
    )
}

@Composable
fun rememberThemeRipple(
    bounded: Boolean = true
) = rememberRipple(
    color = MaterialTheme.colorScheme.primary,
    bounded = bounded
)