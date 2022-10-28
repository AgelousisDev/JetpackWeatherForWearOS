package com.agelousis.jetpackweatherwearos.network.response

import android.content.Context
import com.agelousis.jetpackweatherwearos.R
import com.google.gson.annotations.SerializedName

data class WeatherAirQualityDataModel(
    @SerializedName(value = "co") val co: Double?,
    @SerializedName(value = "no2") val no2: Double?,
    @SerializedName(value = "o3") val o3: Double?,
    @SerializedName(value = "so2") val so2: Double?,
    @SerializedName(value = "pm2_5") val pm25: Double?,
    @SerializedName(value = "pm10") val pm10: Double?,
    @SerializedName(value = "us-epa-index") val usEpaIndex: Int?,
    @SerializedName(value = "gb-defra-index") val gbDefraIndex: Int?
) {

    infix fun getAirQualityData(
        context: Context
    ) = with(context.resources.getStringArray(R.array.key_air_quality_data_array)) {
        listOf(
            "%.1f - %s".format(co, getOrNull(index = 0)),
            "%.1f - %s".format(no2, getOrNull(index = 1)),
            "%.1f - %s".format(o3, getOrNull(index = 2)),
            "%.1f - %s".format(so2, getOrNull(index = 3))
        )
    }

    infix fun getUsEpaLabel(
        context: Context
    ) = with(context.resources.getStringArray(R.array.key_air_quality_us_epa_index_array)) {
        getOrNull(
            index = usEpaIndex?.let {
                it - 1
            } ?: 0
        )
    }

    val usEpaColor
        get() = when(usEpaIndex) {
            1 -> R.color.green
            2 -> R.color.lightBlue
            3 -> R.color.yellowLighter
            4 -> R.color.yellowDarker
            5 -> R.color.orange
            6 -> R.color.red
            else -> R.color.green
        }

}