package com.agelousis.jetpackweatherwearos.tiles.actionCallback

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModel
import com.agelousis.jetpackweatherwearos.network.repositories.SuccessBlock
import com.agelousis.jetpackweatherwearos.network.response.WeatherResponseModel
import com.agelousis.jetpackweatherwearos.tiles.WeatherWearTileService
import com.agelousis.jetpackweatherwearos.utils.extensions.jsonString
import com.agelousis.jetpackweatherwearos.utils.helpers.LocationHelper
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.weather.WeatherActivity
import com.agelousis.jetpackweatherwearos.weather.viewModel.WeatherViewModel
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WeatherTileActionCallback : ActionCallback {

    private val scope = CoroutineScope(context = Dispatchers.Default)

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        if (
            parameters.getOrDefault(
                key = WeatherWearTileService.appStartActionParam,
                defaultValue = false
            )
        )
            context.startActivity(
                Intent(
                    context,
                    WeatherActivity::class.java
                ).also { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        requestLocationIfGranted(
            context = context
        ) { weatherResponsePair ->
            scope.launch {
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
                    it.toMutablePreferences()
                        .apply {
                            this[PreferencesStoreHelper.CURRENT_ADDRESS_DATA_KEY] = weatherResponsePair.first.jsonString
                                ?: ""
                            this[PreferencesStoreHelper.WEATHER_RESPONSE_MODE_DATA_KEY] = weatherResponsePair.second.jsonString
                                ?: ""
                        }
                }
                //WeatherAppWidget().update(context, glanceId)
            }
        }
    }

    private suspend fun requestLocationIfGranted(
        context: Context,
        successBlock: SuccessBlock<Pair<AddressDataModel, WeatherResponseModel>>
    ) {
        val preferencesStoreHelper = PreferencesStoreHelper(
            context = context
        )
        val addressDataModel = preferencesStoreHelper.currentAddressDataModel.firstOrNull()
        LocationHelper(
            context = context,
            priority = Priority.PRIORITY_HIGH_ACCURACY,
            locationPermissionsDeclinedBlock = {
                requestWeatherForecast(
                    context = context,
                    addressDataModel = addressDataModel
                        ?: return@LocationHelper,
                    successBlock = successBlock
                )
            },
        ) { location ->
            LocationHelper.getAddressFromLocation(
                context = context,
                longitude = location.longitude,
                latitude = location.latitude,
            ) { addressDataModel ->
                requestWeatherForecast(
                    context = context,
                    addressDataModel = addressDataModel
                        ?: return@getAddressFromLocation,
                    successBlock = successBlock
                )
            }
        }
    }

    private fun requestWeatherForecast(
        context: Context,
        addressDataModel: AddressDataModel,
        successBlock: SuccessBlock<Pair<AddressDataModel, WeatherResponseModel>>
    ) {
        WeatherViewModel().requestCurrentWeather(
            context = context,
            location = "%f,%f".format(
                addressDataModel.latitude
                    ?: return,
                addressDataModel.longitude
            ),
            airQualityState = true,
            successBlock = { weatherResponseModel ->
                successBlock(
                    addressDataModel to weatherResponseModel
                )
            }
        )
    }


}