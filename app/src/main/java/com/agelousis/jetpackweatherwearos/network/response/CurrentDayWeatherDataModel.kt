package com.agelousis.jetpackweatherwearos.network.response

import android.content.Context
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType
import com.google.gson.annotations.SerializedName

data class CurrentDayWeatherDataModel(
    @SerializedName(value = "maxtemp_c") val maxTempC: Double?,
    @SerializedName(value = "maxtemp_f") val maxTempF: Double?,
    @SerializedName(value = "mintemp_c") val minTempC: Double?,
    @SerializedName(value = "mintemp_f") val minTempF: Double?,
    @SerializedName(value = "avgtemp_c") val avgTempC: Double?,
    @SerializedName(value = "avgtemp_f") val avgTempF: Double?,
    @SerializedName(value = "maxwind_mph") val maxWindMph: Double?,
    @SerializedName(value = "maxwind_kph") val maxWindKph: Double?,
    @SerializedName(value = "totalprecip_mm") val totalPrecipMm: Double?,
    @SerializedName(value = "totalprecip_in") val totalPrecipIn: Double?,
    @SerializedName(value = "avgvis_km") val avgVisKm: Double?,
    @SerializedName(value = "avgvis_miles") val avgVisMiles: Double?,
    @SerializedName(value = "avghumidity") val avgHumidity: Double?,
    @SerializedName(value = "daily_will_it_rain") val dailyWillItRain: Double?,
    @SerializedName(value = "daily_chance_of_rain") val dailyChanceOfRain: Double?,
    @SerializedName(value = "daily_will_it_snow") val dailyWillItSnow: Double?,
    @SerializedName(value = "daily_chance_of_snow") val dailyChanceOfSnow: Double?,
    @SerializedName(value = "condition") val weatherConditionDataModel: WeatherConditionDataModel?,
    @SerializedName(value = "uv") val uv: Double?
) {

    infix fun currentMinMaxTemperatureUnitFormatted(
        temperatureUnitType: TemperatureUnitType?
    ) =
        when(temperatureUnitType) {
            TemperatureUnitType.FAHRENHEIT ->
                "%d °F - %d °F".format(
                    minTempF?.toInt() ?: 0,
                    maxTempF?.toInt() ?: 0
                )
            else ->
                "%d °C - %d °C".format(
                    minTempC?.toInt() ?: 0,
                    maxTempC?.toInt() ?: 0
                )
        }

    infix fun getWindStateWarning(
        context: Context
    ): String = with(context.resources.getStringArray(R.array.key_wind_speed_warnings_array)) {
        when (maxWindKph?.toInt() ?: 0) {
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

    val windStateColor
        get() = when(maxWindKph?.toInt() ?: 0) {
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

}