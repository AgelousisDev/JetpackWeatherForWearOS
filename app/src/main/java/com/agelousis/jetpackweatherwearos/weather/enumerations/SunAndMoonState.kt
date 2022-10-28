package com.agelousis.jetpackweatherwearos.weather.enumerations

import com.agelousis.jetpackweatherwearos.R

enum class SunAndMoonState {
    SUNRISE,
    MOONRISE,
    SUNSET,
    MOON_SET;

    val icon
        get() = when(this) {
            SUNRISE -> R.drawable.ic_sunrise_morning
            MOONRISE -> R.drawable.ic_moonrise
            SUNSET -> R.drawable.ic_sunset
            MOON_SET -> R.drawable.ic_moonset
        }

    val labelResourceId
        get() = when(this) {
            SUNRISE -> R.string.key_sunrise_label
            MOONRISE -> R.string.key_moonrise_label
            SUNSET -> R.string.key_sunset_label
            MOON_SET -> R.string.key_moon_set_label
        }

}