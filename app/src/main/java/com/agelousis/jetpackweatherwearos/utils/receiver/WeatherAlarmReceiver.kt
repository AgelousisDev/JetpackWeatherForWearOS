package com.agelousis.jetpackweatherwearos.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.agelousis.jetpackweatherwearos.network.response.CurrentWeatherDataModel
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModel
import com.agelousis.jetpackweatherwearos.utils.helpers.NotificationHelper
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.utils.helpers.UrlBitmapHelper
import com.agelousis.jetpackweatherwearos.utils.model.NotificationDataModel
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType
import com.agelousis.jetpackweatherwearos.weather.viewModel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WeatherAlarmReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0 ?: return
        val preferencesStoreHelper = PreferencesStoreHelper(
            context = p0
        )
        scope.launch {
            val addressDataModel = preferencesStoreHelper.currentAddressDataModel.firstOrNull()
            val weatherResponseModel = preferencesStoreHelper.weatherResponseModelData.firstOrNull()
            val temperatureUnitType = preferencesStoreHelper.temperatureUnitType.firstOrNull()

            weatherResponseModel?.weatherForecastDataModel?.todayWeatherForecastDayDataModel?.currentWeatherDataModel?.let { currentWeatherDataModel ->
                openScopeAndScheduleNotification(
                    context = p0,
                    temperatureUnitType = temperatureUnitType,
                    addressDataModel = addressDataModel
                        ?: return@let,
                    currentWeatherDataModel = currentWeatherDataModel
                )
            } ?: WeatherViewModel().requestCurrentWeather(
                context = p0,
                location = "%f,%f".format(
                    addressDataModel?.latitude
                        ?: return@launch,
                    addressDataModel.longitude
                ),
                airQualityState = true
            ) {
                openScopeAndScheduleNotification(
                    context = p0,
                    temperatureUnitType = temperatureUnitType,
                    addressDataModel = addressDataModel,
                    currentWeatherDataModel = it.currentWeatherDataModel
                        ?: return@requestCurrentWeather
                )
            }
        }
    }

    private fun openScopeAndScheduleNotification(
        context: Context,
        temperatureUnitType: TemperatureUnitType?,
        addressDataModel: AddressDataModel,
        currentWeatherDataModel: CurrentWeatherDataModel
    ) {
        scope.launch {
            UrlBitmapHelper.init(
                urlString = currentWeatherDataModel.weatherConditionDataModel?.iconUrl
                    ?: return@launch
            ) { iconBitmap ->
                NotificationHelper.triggerNotification(
                    context = context,
                    notificationDataModel = NotificationDataModel(
                        notificationId = (0..1000).random(),
                        title = addressDataModel.addressLine,
                        body = "%s\n%s\n%s\n%s\n%s".format(
                            currentWeatherDataModel.currentTemperatureUnitFormatted(
                                temperatureUnitType = temperatureUnitType
                                    ?: TemperatureUnitType.CELSIUS
                            ),
                            currentWeatherDataModel.weatherConditionDataModel.text,
                            "%s %s".format(
                                currentWeatherDataModel.windKph?.toInt()
                                    ?.toString()
                                    ?: "",
                                context.resources.getString(R.string.key_km_hourly_label)
                            ),
                            currentWeatherDataModel.getWindStateWarning(
                                context = context
                            ),
                            context.resources.getString(
                                R.string.key_now_with_value_label,
                                currentWeatherDataModel.getWindDirection(
                                    context = context
                                )
                            )
                        ),
                        largeImageBitmap = iconBitmap
                    )
                )
            }
        }
    }

}