package com.agelousis.jetpackweatherwearos.weather.bottomNavigation

import androidx.annotation.StringRes
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.weather.drawerNavigation.WeatherDrawerNavigationScreen

sealed class WeatherNavigationScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: Int,
    var badge: String? = null
) {

    companion object {

        private val values
            get() = arrayOf(
                Today,
                Tomorrow,
                NextDays,
                Alerts
            )

        infix fun fromRoute(
            route: String
        ) = values.firstOrNull { weatherNavigationScreen ->
            weatherNavigationScreen.route == route
        }

    }

    val weatherDrawerNavigationScreen
        get() = WeatherDrawerNavigationScreen.values.firstOrNull { weatherDrawerNavigationScreen ->
            weatherDrawerNavigationScreen.route == route
        }

    object Today: WeatherNavigationScreen(
        route = "todayRoute",
        resourceId = R.string.key_today_label,
        icon = R.drawable.ic_today
    )
    object Tomorrow: WeatherNavigationScreen(
        route = "tomorrowRoute",
        resourceId = R.string.key_tomorrow_label,
        icon = R.drawable.ic_tomorrow
    )
    object NextDays: WeatherNavigationScreen(
        route = "nextDaysRoute",
        resourceId = R.string.key_next_days,
        icon = R.drawable.ic_next_days
    )
    object Alerts: WeatherNavigationScreen(
        route = "alertsRoute",
        resourceId = R.string.key_alerts_label,
        icon = R.drawable.ic_warning_alert
    )
}