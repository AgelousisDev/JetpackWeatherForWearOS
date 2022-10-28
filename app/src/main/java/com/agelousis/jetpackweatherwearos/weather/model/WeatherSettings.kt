package com.agelousis.jetpackweatherwearos.weather.model

import android.content.Context
import androidx.annotation.StringRes
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.utils.enumerations.LanguageEnum
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType

sealed class WeatherSettings(
    @StringRes val label: Int
) {
    var optionModelList: List<OptionModel>? = null
    var selectedOptionModel: OptionModel? = null
    var optionIsChecked = false
    var optionIsEnabled = true

    object TemperatureType: WeatherSettings(
        label = R.string.key_temperature_unit_label
    ) {

        fun with(
            context: Context,
            temperatureUnitType: TemperatureUnitType?
        ) = this.apply {
            optionModelList = context.resources.getStringArray(R.array.key_temperature_unit_types_array).mapIndexed { index, item ->
                OptionModel(
                    label = item,
                    icon = if (index == 0) R.drawable.ic_celsius else R.drawable.ic_fahrenheit
                )
            }
            selectedOptionModel = temperatureUnitType?.let {
                OptionModel(
                    label = context.resources.getStringArray(R.array.key_temperature_unit_types_array)[TemperatureUnitType.values().indexOf(it)],
                    icon = it.icon
                )
            }
        }

    }
    object OfflineMode: WeatherSettings(
        label = R.string.key_load_saved_weather_when_offline_label
    ) {

        infix fun isEnabled(
            offlineMode: Boolean
        ) = this.apply {
            optionIsChecked = offlineMode
        }

    }

    object WeatherNotifications: WeatherSettings(
        label = R.string.key_weather_notifications_label
    ) {

        infix fun isEnabled(
            weatherNotificationsState: Boolean
        ) = this.apply {
            optionIsChecked = weatherNotificationsState
            //optionIsEnabled = sharedPreferences.addressDataModel != null
        }

    }

    object WeatherLanguage: WeatherSettings(
        label = R.string.key_language_label
    ) {

        fun with(
            context: Context,
            languageEnum: LanguageEnum?
        ) = this.apply {
            optionModelList = (LanguageEnum languagesFrom context).mapIndexed { index, item ->
                OptionModel(
                    label = item,
                    iconUrl = LanguageEnum.values()[index].iconUrl
                )
            }
            selectedOptionModel = languageEnum?.let {
                OptionModel(
                    label = it labelFrom context,
                    iconUrl = it.iconUrl
                )
            }
        }

    }

}