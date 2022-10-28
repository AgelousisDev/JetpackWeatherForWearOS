package com.agelousis.jetpackweatherwearos.weather.drawerNavigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.agelousis.jetpackweatherwearos.R

sealed class WeatherDrawerNavigationScreen(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {

    companion object {

        val values
            get() = arrayOf(
                HomeWeather,
                Settings
            )

        infix fun fromRoute(
            route: String
        ) = values.firstOrNull { weatherDrawerNavigationScreen ->
            weatherDrawerNavigationScreen.route == route
        }

    }

    object HomeWeather: WeatherDrawerNavigationScreen(
        route = "todayRoute",
        icon = Icons.Filled.Home,
        label = R.string.key_weather_label
    )
    object Settings: WeatherDrawerNavigationScreen(
        route = "settingsRoute",
        icon = Icons.Filled.Settings,
        label = R.string.key_settings_label
    )
}