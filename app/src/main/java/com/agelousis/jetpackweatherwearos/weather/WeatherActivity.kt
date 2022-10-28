/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */
package com.agelousis.jetpackweatherwearos.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agelousis.jetpackweatherwearos.ui.theme.JetpackWeatherForWearOSTheme
import com.agelousis.jetpackweatherwearos.weather.ui.WeatherActivityLayout
import com.agelousis.jetpackweatherwearos.weather.viewModel.WeatherViewModel

class WeatherActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackWeatherForWearOSTheme {
                val viewModel = viewModel<WeatherViewModel>()
                WeatherActivityLayout(
                    viewModel = viewModel
                )
            }
        }
    }

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