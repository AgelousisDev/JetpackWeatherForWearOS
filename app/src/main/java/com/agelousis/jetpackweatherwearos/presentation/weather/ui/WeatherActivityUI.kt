package com.agelousis.jetpackweatherwearos.presentation.weather.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agelousis.jetpackweatherwearos.presentation.theme.JetpackWeatherForWearOSTheme
import com.agelousis.jetpackweatherwearos.presentation.weather.viewModel.WeatherViewModel

@Composable
fun WeatherActivityLayout(
    viewModel: WeatherViewModel
) {

}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    JetpackWeatherForWearOSTheme {
        WeatherActivityLayout(
            viewModel = viewModel()
        )
    }
}
