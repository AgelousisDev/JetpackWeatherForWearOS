package com.agelousis.jetpackweatherwearos.network.response

import com.google.gson.annotations.SerializedName

data class WeatherLocationDataModel(
    @SerializedName(value = "name") val name: String?,
    @SerializedName(value = "region") val region: String?,
    @SerializedName(value = "country") val country: String?,
    @SerializedName(value = "lat") val lat: Double?,
    @SerializedName(value = "lon") val lon: Double?,
    @SerializedName(value = "tz_id") val tz_id: String?,
    @SerializedName(value = "localtime_epoch") val localtime_epoch: Long?,
    @SerializedName(value = "localtime") val localtime: String?
) {

    val regionCountry
        get() = "%s, %s".format(
            region ?: "",
            country ?: ""
        )

}