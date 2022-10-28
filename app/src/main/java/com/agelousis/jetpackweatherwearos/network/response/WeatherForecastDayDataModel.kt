package com.agelousis.jetpackweatherwearos.network.response

import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherForecastDayDataModel(
    @SerializedName(value = "date") val date: String?,
    @SerializedName(value = "date_epoch") val dateEpoch: String?,
    @SerializedName(value = "day") val currentDayWeatherDataModel: CurrentDayWeatherDataModel?,
    @SerializedName(value = "astro") val weatherAstroDataModel: WeatherAstroDataModel?,
    @SerializedName(value = "hour") val weatherHourlyDataModelList: List<WeatherHourlyDataModel>?
) {

    val remainingWeatherHourlyDataModelList
        get() = weatherHourlyDataModelList?.filter { weatherHourlyDataModel ->
            (weatherHourlyDataModel.time?.toDate() ?: Date()) > Date()
        }

    private val currentWeatherHourlyDataModel
        get() = weatherHourlyDataModelList?.firstOrNull { weatherHourlyDataModel ->
            val nowCalendar = Calendar.getInstance()
            nowCalendar.time = Date()

            val weatherHourlyCalendar = Calendar.getInstance()
            weatherHourlyCalendar.time = weatherHourlyDataModel.time?.toDate(
                pattern = Constants.SERVER_DATE_TIME_FORMAT
            ) ?: Date()

            nowCalendar.get(Calendar.HOUR_OF_DAY) == weatherHourlyCalendar.get(Calendar.HOUR_OF_DAY)
        }

    val currentWeatherDataModel
        get() = CurrentWeatherDataModel(
            valpdatedEpoch = null,
            lastUpdated = currentWeatherHourlyDataModel?.time,
            tempC = currentWeatherHourlyDataModel?.tempC,
            tempF = currentWeatherHourlyDataModel?.tempF,
            isDay = currentWeatherHourlyDataModel?.isDay,
            weatherConditionDataModel = currentWeatherHourlyDataModel?.weatherConditionDataModel,
            windMph = currentWeatherHourlyDataModel?.windMph,
            windKph = currentWeatherHourlyDataModel?.windKph,
            windDegree = currentWeatherHourlyDataModel?.windDegree,
            windDir = currentWeatherHourlyDataModel?.windDir,
            pressureMb = currentWeatherHourlyDataModel?.pressureMb,
            pressureIn = currentWeatherHourlyDataModel?.pressureIn,
            precipIn = currentWeatherHourlyDataModel?.precipIn,
            precipMm = currentWeatherHourlyDataModel?.precipMm,
            humidity = currentWeatherHourlyDataModel?.humidity,
            cloud = currentWeatherHourlyDataModel?.cloud,
            feelsLikeC = currentWeatherHourlyDataModel?.feelsLikeC,
            feelsLikeF = currentWeatherHourlyDataModel?.feelsLikeF,
            visMiles = currentWeatherHourlyDataModel?.visMiles,
            visKm = currentWeatherHourlyDataModel?.visKm,
            uv = currentWeatherHourlyDataModel?.uv,
            gustMph = currentWeatherHourlyDataModel?.gustMph,
            gustKph = currentWeatherHourlyDataModel?.gustKph,
            airQuality = null
        )

}