package com.agelousis.jetpackweatherwearos.utils.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import androidx.core.os.BuildCompat
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.google.gson.Gson
import okio.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun Date.toDisplayDate(
    pattern: String = Constants.DISPLAY_DATE_TIME_FORMAT,
    plusDays: Int = 0
): String = with(SimpleDateFormat(pattern, Locale.getDefault())) {
    return if (plusDays > 0) {
        val calendar = Calendar.getInstance()
        calendar.time = this@toDisplayDate
        calendar.add(Calendar.DAY_OF_YEAR, plusDays)
        format(calendar.time)
    }
    else
        format(this@toDisplayDate)
}

fun String.toDate(
    pattern: String = Constants.SERVER_DATE_TIME_FORMAT
): Date? = with(SimpleDateFormat(pattern, Locale.ENGLISH)) {
    try {
        parse(this@toDate)
    }
    catch (e: Exception) {
        null
    }
}

inline fun <reified T : Enum<*>> valueEnumOrNull(name: String?): T? =
    T::class.java.enumConstants?.firstOrNull {
        it.name.lowercase() == name?.lowercase()
    }

val Date.fullTime: String
    get() = with(SimpleDateFormat(Constants.FULL_TIME_FORMAT, Locale.getDefault())) {
        format(this@fullTime)
    }

inline fun Int.run(block: (index: Int) -> Unit) {
    for (index in 0 until this)
        block(index)
}

inline fun <reified T> String.toModel() =
    try {
        Gson().fromJson(this, T::class.java)
    }
    catch (e: Exception) {
        null
    }

val <T> T.jsonString
    get() = try {
        Gson().toJson(this)
    }
    catch (e: Exception) {
        null
    }

inline fun <T : Any> ifLetThen(vararg elements: T?, closure: (List<Any>) -> Unit): List<Any>? {
    return if (elements.all { it != null }) {
        closure(elements.filterNotNull())
        elements.filterNotNull()
    }
    else null
}

val String.urlBitmap
    get() =
        try {
            val url = URL(this)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (ex: IOException) {
            null
        }

fun Drawable.fromVector(padding: Int = 0): Bitmap {
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(padding, padding, canvas.width - padding, canvas.height - padding)
    this.draw(canvas)
    return bitmap
}

infix fun Bitmap.rotate(
    angle: Float
): Bitmap = Bitmap.createBitmap(
    this,
    0,
    0,
    width,height,
    Matrix().also { matrix ->
        matrix.postRotate(angle)
    },
    true
)

val isAndroid13
    @SuppressLint("UnsafeOptInUsageError")
    get() = BuildCompat.isAtLeastT()