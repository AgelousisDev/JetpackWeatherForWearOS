package com.agelousis.jetpackweatherwearos.tiles

import androidx.compose.runtime.Composable
import androidx.glance.action.ActionParameters
import androidx.glance.wear.tiles.GlanceTileService
import com.agelousis.jetpackweatherwearos.tiles.ui.WeatherTileLayout

class WeatherWearTileService: GlanceTileService() {

    companion object {
        private const val APP_START_ACTION_PARAM = "appStartActionParam"
        val appStartActionParam = ActionParameters.Key<Boolean>(name = APP_START_ACTION_PARAM)
    }

    @Composable
    override fun Content() {
        WeatherTileLayout()
    }

}