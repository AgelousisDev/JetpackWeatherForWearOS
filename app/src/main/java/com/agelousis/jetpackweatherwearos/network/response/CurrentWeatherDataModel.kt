package com.agelousis.jetpackweatherwearos.network.response

import android.content.Context
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.utils.extensions.getLocalizedArray
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class CurrentWeatherDataModel(
    @SerializedName(value = "last_updated_epoch") val valpdatedEpoch: Long? = null,
    @SerializedName(value = "last_updated") val lastUpdated: String? = null,
    @SerializedName(value = "temp_c") val tempC: Double? = null,
    @SerializedName(value = "temp_f") val tempF: Double? = null,
    @SerializedName(value = "is_day") val isDay: Int? = null,
    @SerializedName(value = "condition") val weatherConditionDataModel: WeatherConditionDataModel? = null,
    @SerializedName(value = "wind_mph") val windMph: Double? = null,
    @SerializedName(value = "wind_kph") val windKph: Double? = null,
    @SerializedName(value = "wind_degree") val windDegree: Int? = null,
    @SerializedName(value = "wind_dir") val windDir: String? = null,
    @SerializedName(value = "pressure_mb") val pressureMb: Double? = null,
    @SerializedName(value = "pressure_in") val pressureIn: Double? = null,
    @SerializedName(value = "precip_mm") val precipMm: Double? = null,
    @SerializedName(value = "precip_in") val precipIn: Double? = null,
    @SerializedName(value = "humidity") val humidity: Int? = null,
    @SerializedName(value = "cloud") val cloud: Int? = null,
    @SerializedName(value = "feelslike_c") val feelsLikeC: Double? = null,
    @SerializedName(value = "feelslike_f") val feelsLikeF: Double? = null,
    @SerializedName(value = "vis_km") val visKm: Double? = null,
    @SerializedName(value = "vis_miles") val visMiles: Double? = null,
    @SerializedName(value = "uv") val uv: Double? = null,
    @SerializedName(value = "gust_mph") val gustMph: Double? = null,
    @SerializedName(value = "gust_kph") val gustKph: Double? = null,
    @SerializedName(value = "air_quality") val airQuality: WeatherAirQualityDataModel? = null,
) {

    infix fun currentTemperatureUnitFormatted(
        temperatureUnitType: TemperatureUnitType?
    ) =
        when(temperatureUnitType) {
            TemperatureUnitType.FAHRENHEIT ->
                "%d °F".format(
                    tempF?.toInt() ?: 0
                )
            else ->
                "%d °C".format(
                    tempC?.toInt() ?: 0
                )
        }

    infix fun feelsLikeTemperatureUnitFormatted(
        temperatureUnitType: TemperatureUnitType?
    ) =
        when(temperatureUnitType) {
            TemperatureUnitType.FAHRENHEIT ->
                "%d °F".format(
                    feelsLikeF?.toInt() ?: 0
                )
            else ->
                "%d °C".format(
                    feelsLikeC?.toInt() ?: 0
                )
        }

    private val isDayBool
        get() = isDay == 1

    val dayStateAnimationResourceId
        get() = if (isDayBool) R.raw.day_animation else R.raw.night_animation

    val windStateColor
        get() = when(windKph?.toInt() ?: 0) {
            in 0 until 20 ->
                R.color.blue
            in 20 until 30 ->
                R.color.teal_700
            in 30 until 50 ->
                R.color.green
            in 50 until 100 ->
                R.color.yellowDarker
            else ->
                R.color.red
        }

    fun getWindStateWarning(
        context: Context
    ): String = with(context.resources.getStringArray(R.array.key_wind_speed_warnings_array)) {
        when (windKph?.toInt() ?: 0) {
            in 0 until 20 ->
                first()
            in 20 until 30 ->
                this[1]
            in 30 until 50 ->
                this[2]
            in 50 until 100 ->
                this[3]
            else ->
                this[4]
        }
    }

    infix fun getWindDirection(
        context: Context
    ) = with(windDir) {
        val windDirectionsInBaseLocaleArray = context.getLocalizedArray(
            language = "en",
            resourceId = R.array.key_wind_directions_array
        )
        val windDirectionsArray = context.resources.getStringArray(R.array.key_wind_directions_array)
        val windDirectionsBuilder = StringBuilder()
        for (windDirection in (windDir?.toCharArray()?.take(n = 2)?.distinct() ?: listOf())) {
            val windDirectionsArrayIndex = windDirectionsInBaseLocaleArray.indexOfFirst {
                it.startsWith(
                    prefix = windDirection.toString(),
                    ignoreCase = true
                )
            }.takeIf {
                it > -1
            } ?: continue
            windDirectionsBuilder.append(
                windDirectionsArray[windDirectionsArrayIndex]
            )
        }
        windDirectionsBuilder.toString()
    }

    val uvIndexColor
        get() = when(uv?.toInt() ?: 0) {
            in 0..2 ->
                R.color.green
            in 3..5 ->
                R.color.yellowDarker
            in 6..7 ->
                R.color.orange
            in 8..10 ->
                R.color.red
            else ->
                R.color.violet
        }

    infix fun getUvIndexExposureLevel(
        context: Context
    ): String = with(context.resources.getStringArray(R.array.key_uv_index_levels_array)) {
        val exposureLevel = when(uv?.toInt() ?: 0) {
            in 0..2 ->
                0
            in 3..5 ->
                1
            in 6..7 ->
                2
            in 8..10 ->
                3
            else ->
                4
        }
        this[exposureLevel]
    }

}