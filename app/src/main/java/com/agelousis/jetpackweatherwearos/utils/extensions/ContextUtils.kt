package com.agelousis.jetpackweatherwearos.utils.extensions

import android.app.AlarmManager
import android.app.LocaleManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.agelousis.jetpackweatherwearos.utils.receiver.WeatherAlarmReceiver
import java.util.*

fun Context.arePermissionsGranted(
    vararg permissions: String
): Boolean {
    var isGranted = false
    for (permission in permissions)
        isGranted = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    return isGranted
}

/*infix fun Context.openWebViewIntent(urlString: String) {
    try {
        val uri = Uri.parse(urlString)
        val intentBuilder = CustomTabsIntent.Builder()
        val chromeIntent = intentBuilder.build()
        chromeIntent.intent.setPackage("com.android.chrome")
        chromeIntent.launchUrl(this, uri)
    }
    catch(e: Exception) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            )
        }
        catch(_: Exception) {}
    }
}*/

fun Context.schedulePushNotificationsEvery(
    scheduleState: Boolean,
    alarmManagerType: Long
) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmPendingIntent = PendingIntent.getBroadcast(
        this,
        0,
        Intent(this, WeatherAlarmReceiver::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    if (scheduleState)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis,
            alarmManagerType,
            alarmPendingIntent
        )
    else
        alarmManager.cancel(alarmPendingIntent)
}

/*infix fun Context.bitmapDescriptorFromVector(
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(this, vectorResId)
        ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}*/

fun Context.getLocalizedString(
    language: String,
    resourceId: Int
): String {
    val configuration = this getLocalizedConfiguration language
    return createConfigurationContext(configuration).resources.getString(resourceId)
}

fun Context.getLocalizedArray(
    language: String,
    resourceId: Int
): Array<String> {
    val configuration = this getLocalizedConfiguration language
    return createConfigurationContext(configuration).resources.getStringArray(resourceId)
}

private infix fun Context.getLocalizedConfiguration(
    language: String
): Configuration {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale(language))
    return configuration
}

/*infix fun Context.setAppLanguage(
    language: String
) {
    if (isAndroid13)
        (getSystemService(Context.LOCALE_SERVICE) as? LocaleManager)?.applicationLocales = LocaleList(Locale.forLanguageTag(language))
    else
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
}

val WindowSizeClass.weatherDrawerNavigationType
    get() = when(this.widthSizeClass) {
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded ->
            WeatherDrawerNavigationType.PERMANENT_NAVIGATION_DRAWER
        else ->
            WeatherDrawerNavigationType.NORMAL
    }*/