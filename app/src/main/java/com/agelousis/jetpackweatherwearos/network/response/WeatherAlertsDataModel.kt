package com.agelousis.jetpackweatherwearos.network.response

import com.google.gson.annotations.SerializedName

data class WeatherAlertsDataModel(
    @SerializedName(value = "alert") val weatherAlertsModelList: List<WeatherAlertModel>
)