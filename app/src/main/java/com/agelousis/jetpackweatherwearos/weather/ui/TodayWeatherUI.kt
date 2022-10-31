package com.agelousis.jetpackweatherwearos.weather.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.ui.theme.Typography
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.agelousis.jetpackweatherwearos.utils.extensions.toDisplayDate
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.weather.enumerations.TemperatureUnitType
import com.agelousis.jetpackweatherwearos.weather.viewModel.WeatherViewModel

typealias RefreshWeatherBlock = () -> Unit

@Composable
fun TodayWeatherLayout(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    refreshWeatherBlock: RefreshWeatherBlock
) {
    val context = LocalContext.current
    val addressDataModel by viewModel.addressDataModelStateFlow.collectAsState()
    val weatherResponseModel by viewModel.weatherResponseLiveData.observeAsState()
    val preferencesStorageHelper = PreferencesStoreHelper(
        context = context
    )
    val temperatureUnitType by preferencesStorageHelper.temperatureUnitType.collectAsState(
        initial = TemperatureUnitType.CELSIUS
    )
    if (addressDataModel == null
        || weatherResponseModel == null)
        Box(
            modifier = modifier
        )
    else
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp
            ),
            modifier = modifier
                .padding(
                    vertical = 24.dp
                )
        ) {
            // Location
            Text(
                text = addressDataModel?.addressLine
                    ?: "",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(
                        id = R.color.white
                    )
                ),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp
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
                    style = Typography.caption2,
                    color = colorResource(
                        id = R.color.white
                    )
                )
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_refresh_24
                    ),
                    tint = colorResource(
                        id = R.color.white
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            size = 20.dp
                        )
                        .clickable(
                            onClick = refreshWeatherBlock
                        )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp
                ),
                modifier = Modifier
                    .background(
                        color = colorResource(
                            id = R.color.colorSecondaryContainer
                        ),
                        shape = RoundedCornerShape(
                            size = 8.dp
                        )
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
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
                        style = Typography.body1,
                        color = colorResource(
                            id =  R.color.white
                        )
                    )
                    // Condition Icon
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest
                                .Builder(
                                    context = LocalContext.current
                                )
                                .data(
                                    data = weatherResponseModel?.currentWeatherDataModel?.weatherConditionDataModel?.iconUrl
                                )
                                .crossfade(
                                    enable = true
                                )
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(
                                size = 40.dp
                            )
                    )
                }
                // Condition Text
                Text(
                    text = weatherResponseModel?.currentWeatherDataModel?.weatherConditionDataModel?.text
                        ?: "",
                    style = Typography.body2,
                    color = colorResource(
                        id = R.color.white
                    ),
                    modifier = Modifier
                        .padding(
                            all = 8.dp
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
                    style = Typography.caption2,
                    color = colorResource(
                        id = R.color.white
                    ),
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                )
            }
            // Wind Layout
            Row(
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp
                )
            ) {
                Text(
                    text = weatherResponseModel?.currentWeatherDataModel?.windKph?.toInt()?.toString()
                        ?: "",
                    style = Typography.body2,
                    color = colorResource(
                        id = weatherResponseModel?.currentWeatherDataModel?.windStateColor
                            ?: R.color.white
                    )
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp
                    )
                ) {
                    if (weatherResponseModel?.currentWeatherDataModel?.windDegree != null) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_arrow_direction_down
                            ),
                            contentDescription = null,
                            tint = colorResource(
                                id = weatherResponseModel?.currentWeatherDataModel?.windStateColor
                                    ?: R.color.white
                            ),
                            modifier = Modifier
                                .size(
                                    size = 15.dp
                                )
                                .rotate(
                                    degrees = weatherResponseModel?.currentWeatherDataModel?.windDegree?.toFloat()
                                        ?: 0f
                                )
                        )
                    }
                    Text(
                        text = context.resources.getString(R.string.key_km_hourly_label),
                        style = Typography.caption2,
                        color = colorResource(
                            id = R.color.white
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp
                    )
                ) {
                    Text(
                        text = weatherResponseModel?.currentWeatherDataModel?.getWindStateWarning(
                            context = context
                        ) ?: "",
                        style = Typography.caption2,
                        color = colorResource(
                            id = weatherResponseModel?.currentWeatherDataModel?.windStateColor
                                ?: R.color.white
                        )
                    )
                    Text(
                        text = context.resources.getString(
                            R.string.key_now_with_value_label,
                            if (LocalInspectionMode.current)
                                "North"
                            else
                                weatherResponseModel?.currentWeatherDataModel?.getWindDirection(
                                    context = context
                                ) ?: ""
                        ),
                        style = Typography.caption2,
                        color = colorResource(
                            id = R.color.white
                        )
                    )
                }
            }
        }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun TodayWeatherLayoutPreview() {
    TodayWeatherLayout(
        viewModel = viewModel<WeatherViewModel>().apply {
            weatherResponseMutableLiveData.value = Constants.sampleWeatherResponseModel
        }
    ) {}
}