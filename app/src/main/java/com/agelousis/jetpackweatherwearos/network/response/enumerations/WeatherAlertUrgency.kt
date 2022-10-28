package com.agelousis.jetpackweatherwearos.network.response.enumerations

enum class WeatherAlertUrgency(val type: String) {
    EXPECTED(type = "Expected"),
    FUTURE(type = "Future")
}