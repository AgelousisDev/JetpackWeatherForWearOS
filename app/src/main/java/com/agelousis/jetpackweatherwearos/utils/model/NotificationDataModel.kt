package com.agelousis.jetpackweatherwearos.utils.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationDataModel(val notificationId: Int,
                                 val title: String?,
                                 val body: String?,
                                 val largeImageBitmap: Bitmap?
): Parcelable