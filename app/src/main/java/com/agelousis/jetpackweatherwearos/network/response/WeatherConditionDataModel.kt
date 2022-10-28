package com.agelousis.jetpackweatherwearos.network.response

import com.google.gson.annotations.SerializedName

data class WeatherConditionDataModel(
    @SerializedName(value = "text") val text: String?,
    @SerializedName(value = "icon") val icon: String?,
    @SerializedName(value = "code") val code: Int?
) {

    val iconUrl
        get() =
            if (icon?.contains("https:") == true)
                icon
            else
                "%s%s".format(
                    "https:",
                    icon ?: ""
                )

}