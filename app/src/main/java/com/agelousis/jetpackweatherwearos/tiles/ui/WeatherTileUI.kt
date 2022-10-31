package com.agelousis.jetpackweatherwearos.tiles.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.glance.*
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModel
import com.agelousis.jetpackweatherwearos.network.response.WeatherResponseModel
import com.agelousis.jetpackweatherwearos.tiles.WeatherWearTileService
import com.agelousis.jetpackweatherwearos.tiles.actionCallback.WeatherTileActionCallback
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.*
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType

private val weatherAppWidgetGlanceModifier =
    GlanceModifier
        .clickable(
            onClick = actionRunCallback<WeatherTileActionCallback>(
                parameters = actionParametersOf(
                    WeatherWearTileService.appStartActionParam to true
                )
            )
        )

@Composable
fun WeatherTileLayout() {
    val context = LocalContext.current
    val preferences = currentState<Preferences>()
    val weatherResponseModel = preferences[
            PreferencesStoreHelper.WEATHER_RESPONSE_MODE_DATA_KEY
    ]?.toModel<WeatherResponseModel>()
    val addressDataModel = preferences[
            PreferencesStoreHelper.CURRENT_ADDRESS_DATA_KEY
    ]?.toModel<AddressDataModel>()
    val temperatureUnitType = valueEnumOrNull<TemperatureUnitType>(
        name = preferences[PreferencesStoreHelper.TEMPERATURE_UNIT_TYPE_KEY]
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        modifier = weatherAppWidgetGlanceModifier
            .background(
                colorProvider = ColorProvider(
                    resId = R.color.colorPrimaryContainer
                )
            )
            /*.clickable(
                onClick = actionStartActivity(WeatherActivity::class.java)
            )*/
            /*.cornerRadius(
                radius = 24.dp
            )*/
            .padding(
                all = 12.dp
            )
            .appWidgetBackground()
    ) {
        // Location
        Text(
            text = addressDataModel?.addressLine
                ?: "",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = ColorProvider(
                    resId = R.color.colorSecondary
                )
            ),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = weatherAppWidgetGlanceModifier
                .padding(
                    bottom = 8.dp
                )
        ) {
            // Last updated
            Text(
                text = weatherResponseModel?.currentWeatherDataModel?.lastUpdated?.toDate(
                    pattern = Constants.SERVER_DATE_TIME_FORMAT
                )?.toDisplayDate(
                    pattern = Constants.DISPLAY_DATE_TIME_FORMAT
                )?.let {
                    context.resources.getString(
                        R.string.key_last_updated_with_date_label,
                        it
                    )
                } ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = ColorProvider(
                        resId = R.color.colorTertiary
                    )
                ),
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        top = 8.dp,
                        end = 8.dp
                    )
            )
            Image(
                provider = ImageProvider(
                    R.drawable.ic_baseline_refresh_24
                ),
                contentDescription = null,
                modifier = GlanceModifier
                    .size(
                        size = 20.dp
                    )
                    .clickable(
                        onClick = actionRunCallback<WeatherTileActionCallback>()
                    )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = weatherAppWidgetGlanceModifier
                .background(
                    colorProvider = ColorProvider(
                        resId = R.color.colorSecondaryContainer
                    )
                )
                .cornerRadius(
                    radius = 8.dp
                )
        ) {
            Row(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top,
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp
                    )
            ) {
                // C
                Text(
                    text = weatherResponseModel?.currentWeatherDataModel?.currentTemperatureUnitFormatted(
                        temperatureUnitType = temperatureUnitType
                    ) ?: "",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = ColorProvider(
                            resId = R.color.colorSecondary
                        )
                    ),
                    modifier = weatherAppWidgetGlanceModifier
                )
                val iconBitmap = weatherResponseModel?.currentWeatherDataModel?.weatherConditionDataModel?.iconUrl?.urlBitmap
                if (iconBitmap != null)
                // Condition Icon
                    Image(
                        provider = ImageProvider(
                            bitmap = iconBitmap
                        ),
                        contentDescription = null,
                        modifier = weatherAppWidgetGlanceModifier
                            .padding(
                                start = 16.dp
                            )
                            .size(
                                size = 40.dp
                            )
                    )
            }
            // Condition Text
            Text(
                text = weatherResponseModel?.currentWeatherDataModel?.weatherConditionDataModel?.text
                    ?: "",
                style = TextStyle(
                    color = ColorProvider(
                        resId = R.color.colorTertiary
                    ),
                    fontSize = 14.sp
                ),
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp
                    )
            )
            // Feels Like
            Text(
                text = context.resources.getString(
                    R.string.key_feels_like_label,
                    weatherResponseModel?.currentWeatherDataModel?.feelsLikeTemperatureUnitFormatted(
                        temperatureUnitType = temperatureUnitType
                    ) ?: ""
                ),
                style = TextStyle(
                    color = ColorProvider(
                        resId = R.color.colorTertiary
                    ),
                    fontSize = 14.sp
                ),
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        all = 8.dp
                    )
            )
        }
        // Wind Layout
        Row(
            modifier = weatherAppWidgetGlanceModifier
                .padding(
                    top = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weatherResponseModel?.currentWeatherDataModel?.windKph?.toInt()?.toString()
                    ?: "",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = ColorProvider(
                        resId = weatherResponseModel?.currentWeatherDataModel?.windStateColor
                            ?: R.color.colorTertiary
                    )
                ),
                modifier = weatherAppWidgetGlanceModifier
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        start = 16.dp
                    )
            ) {
                if (weatherResponseModel?.currentWeatherDataModel?.windDegree != null) {
                    val arrowBitmap = ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_arrow_direction_down
                    )?.fromVector()?.rotate(
                        angle = weatherResponseModel.currentWeatherDataModel.windDegree.toFloat()
                    )
                    if (arrowBitmap != null)
                        Image(
                            provider = ImageProvider(
                                bitmap = arrowBitmap
                            ),
                            contentDescription = null,
                            modifier = weatherAppWidgetGlanceModifier
                                .size(
                                    size = 15.dp
                                )
                        )
                }
                Text(
                    text = context.resources.getString(R.string.key_km_hourly_label),
                    style = TextStyle(
                        color = ColorProvider(
                            resId = R.color.colorSecondary
                        ),
                        fontSize = 12.sp,
                    ),
                    modifier = weatherAppWidgetGlanceModifier
                        .padding(
                            top = 8.dp
                        )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = weatherAppWidgetGlanceModifier
                    .padding(
                        start = 16.dp
                    )
            ) {
                Text(
                    text = weatherResponseModel?.currentWeatherDataModel?.getWindStateWarning(
                        context = context
                    ) ?: "",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = ColorProvider(
                            resId = weatherResponseModel?.currentWeatherDataModel?.windStateColor
                                ?: R.color.colorSecondary
                        )
                    ),
                    modifier = weatherAppWidgetGlanceModifier
                )
                Text(
                    text = context.resources.getString(
                        R.string.key_now_with_value_label,
                        weatherResponseModel?.currentWeatherDataModel?.getWindDirection(
                            context = context
                        ) ?: ""
                    ),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(
                            resId = R.color.colorSecondary
                        )
                    ),
                    modifier = weatherAppWidgetGlanceModifier
                )
            }
        }
    }
}