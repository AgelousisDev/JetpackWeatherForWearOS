package com.agelousis.jetpackweatherwearos.utils.helpers

import android.app.*
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.agelousis.jetpackweatherwearos.utils.model.NotificationDataModel
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.weather.WeatherActivity

object NotificationHelper {

    fun triggerNotification(
        context: Context,
        notificationDataModel: NotificationDataModel
    ) {
        val channelId = context.resources.getString(R.string.key_notifications_channel_name)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelId, importance)
        notificationManager?.createNotificationChannel(notificationChannel)
        notificationManager?.notify(
            notificationDataModel.notificationId,
            createNotification(
                context = context,
                notificationDataModel = notificationDataModel
            ).build()
        )
    }

    private fun createNotification(
        context: Context,
        notificationDataModel: NotificationDataModel
    ) = NotificationCompat.Builder(
        context,
        context.resources.getString(R.string.key_notifications_channel_name)
    ).apply {
        setContentTitle(notificationDataModel.title)
        //mBuilder.setContentText(notificationDataModel.body)
        setStyle(NotificationCompat.BigTextStyle().bigText(notificationDataModel.body))
        setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        setSmallIcon(R.drawable.ic_weather_partly_cloudy_day)
        notificationDataModel.largeImageBitmap?.let {
            setLargeIcon(it)
        }
        //mBuilder.color = ContextCompat.getColor(context, R.color.colorAccent)
        setDefaults(Notification.DEFAULT_ALL)
        setAutoCancel(true)
        setLights(-0x1450dd, 2000, 2000)
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, WeatherActivity::class.java)/*.also {
                it.putExtra(NotificationActivity.BUBBLE_NOTIFICATION_EXTRA, notificationDataModel)
            }*/,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        setContentIntent(resultPendingIntent)
    }

}