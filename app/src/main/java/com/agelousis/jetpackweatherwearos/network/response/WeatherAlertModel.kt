package com.agelousis.jetpackweatherwearos.network.response

import android.content.Context
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.agelousis.jetpackweatherwearos.utils.extensions.toDisplayDate
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.network.response.enumerations.WeatherAlertSeverity
import com.agelousis.jetpackweatherwearos.network.response.enumerations.WeatherAlertUrgency
import com.google.gson.annotations.SerializedName

data class WeatherAlertModel(
    @SerializedName(value = "headline") val headline: String? = null,
    @SerializedName(value = "msgtype") val msgType: String? = null,
    @SerializedName(value = "severity") val severity: String? = null,
    @SerializedName(value = "urgency") val urgency: String? = null,
    @SerializedName(value = "areas") val areas: String? = null,
    @SerializedName(value = "category") val category: String? = null,
    @SerializedName(value = "event") val event: String? = null,
    @SerializedName(value = "note") val note: String? = null,
    @SerializedName(value = "effective") val effective: String? = null,
    @SerializedName(value = "expires") val expires: String? = null,
    @SerializedName(value = "desc") val desc: String? = null,
    @SerializedName(value = "instruction") val instruction: String? = null
) {

    infix fun getMoreDetailsWith(
        context: Context
    ) = listOf(
        context.resources.getString(
            R.string.key_effective_with_value_label,
            effective?.toDate(
                pattern = Constants.SERVER_FULL_DATE_TIME_FORMAT
            )?.toDisplayDate()
        ),
        context.resources.getString(
            R.string.key_expires_with_value_label,
            expires?.toDate(
                pattern = Constants.SERVER_FULL_DATE_TIME_FORMAT
            )?.toDisplayDate()
        )
    )

    val severityType
        get() = WeatherAlertSeverity.values().firstOrNull { weatherAlertSeverity ->
            weatherAlertSeverity.type == severity
        }

    val urgencyType
        get() = WeatherAlertUrgency.values().firstOrNull { weatherAlertUrgency ->
            weatherAlertUrgency.type == urgency
        }

    val descriptionList
        get() = desc
            ?.replace(
                oldValue = "\n",
                newValue = " "
            )
            ?.split(". ")

    val instructionList
        get() = instruction?.split(
            "."
        )?.filterNot { instruction ->
            instruction.isEmpty()
        }?.map { instruction ->
            instruction.replace(
                oldValue = "\n",
                newValue = " "
            )
        }

}