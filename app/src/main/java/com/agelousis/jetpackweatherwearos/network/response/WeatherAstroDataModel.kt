package com.agelousis.jetpackweatherwearos.network.response

import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.agelousis.jetpackweatherwearos.weather.enumerations.SunAndMoonState
import com.google.gson.annotations.SerializedName

data class WeatherAstroDataModel(
    @SerializedName(value = "sunrise") val sunrise: String?,
    @SerializedName(value = "sunset") val sunset: String?,
    @SerializedName(value = "moonrise") val moonrise: String?,
    @SerializedName(value = "moonset") val moonSet: String?,
    @SerializedName(value = "moon_phase") val moonPhase: String?,
    @SerializedName(value = "moon_illumination") val moonIllumination: String?
) {

    val availableSunAndMoonStates
        get() = listOfNotNull(
            if (sunrise?.toDate(
                pattern = Constants.SMALL_TIME_FORMAT
            ) != null)
                SunAndMoonState.SUNRISE
            else
                null,
            if (moonrise?.toDate(
                    pattern = Constants.SMALL_TIME_FORMAT
                ) != null)
                SunAndMoonState.MOONRISE
            else
                null,
            if (sunset?.toDate(
                    pattern = Constants.SMALL_TIME_FORMAT
                ) != null)
                SunAndMoonState.SUNSET
            else
                null,
            if (moonSet?.toDate(
                    pattern = Constants.SMALL_TIME_FORMAT
                ) != null)
                SunAndMoonState.MOON_SET
            else
                null,
        )

}