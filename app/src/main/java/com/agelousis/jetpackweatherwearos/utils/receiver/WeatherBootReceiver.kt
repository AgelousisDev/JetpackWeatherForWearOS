package com.agelousis.jetpackweatherwearos.utils.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.agelousis.jetpackweatherwearos.utils.extensions.schedulePushNotificationsEvery
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WeatherBootReceiver: BroadcastReceiver() {

    companion object {
        private const val BOOT_COMPLETED_INTENT_ACTION = "android.intent.action.BOOT_COMPLETED"
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(p0: Context?, p1: Intent?) {
        val preferencesStoreHelper = PreferencesStoreHelper(
            context = p0
                ?: return
        )
        if (p1?.action == BOOT_COMPLETED_INTENT_ACTION)
            uiScope.launch {
                if (preferencesStoreHelper.weatherNotificationsState.firstOrNull() == true)
                    p0.schedulePushNotificationsEvery(
                        scheduleState = true,
                        alarmManagerType = AlarmManager.INTERVAL_HOUR
                    )
            }
    }

}