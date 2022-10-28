package com.agelousis.jetpackweatherwearos.network.response

import android.text.format.DateUtils
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.google.gson.annotations.SerializedName

data class WeatherForecastDataModel(
    @SerializedName(value = "forecastday") val weatherForecastDayDataModelList: List<WeatherForecastDayDataModel?>?
) {

    val todayWeatherForecastDayDataModel
        get() = weatherForecastDayDataModelList?.firstOrNull { weatherForecastDayDataModel ->
            DateUtils.isToday(
                weatherForecastDayDataModel?.date?.toDate(
                    pattern = Constants.SERVER_DATE_FORMAT
                )?.time ?: 0L
            )
        }

    //val currentWeatherForecastDayDataModel
        //get() = weatherForecastDayDataModelList?.firstOrNull()

    val nextWeatherForecastDayDataModel
        get() = weatherForecastDayDataModelList?.getOrNull(
            index = 1
        )

    val nextWeatherForecastDayDataModelList
        get() = weatherForecastDayDataModelList?.drop(
            n = 1
        )

}