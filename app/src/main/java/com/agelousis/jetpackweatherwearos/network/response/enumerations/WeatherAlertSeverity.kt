package com.agelousis.jetpackweatherwearos.network.response.enumerations

import com.agelousis.jetpackweatherwearos.R

enum class WeatherAlertSeverity(val type: String) {
    HIGH(type = "High"),
    STRONG(type = "Strong"),
    MODERATE(type = "Moderate"),
    LOW(type = "low"),
    LIGHT(type = "Light");

    val color
        get() = when(this) {
            HIGH,
            STRONG -> R.color.lightRed
            MODERATE -> R.color.orange
            LOW,
            LIGHT -> R.color.lightYellow
        }
}