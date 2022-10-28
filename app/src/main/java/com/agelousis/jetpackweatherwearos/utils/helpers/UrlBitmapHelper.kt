package com.agelousis.jetpackweatherwearos.utils.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

typealias UrlBitmapSuccessBlock = (Bitmap) -> Unit

object UrlBitmapHelper {

    suspend fun init(
        urlString: String,
        urlBitmapSuccessBlock: UrlBitmapSuccessBlock
    ) {
        withContext(Dispatchers.IO) {
            runCatching {
                val urlConnection = URL(urlString).openConnection() as? HttpURLConnection
                urlConnection?.doInput = true
                urlConnection?.connect()
                val inputStream = urlConnection?.inputStream
                val bitmap =  BitmapFactory.decodeStream(inputStream)
                withContext(Dispatchers.Main) inner@ {
                    urlBitmapSuccessBlock(bitmap ?: return@inner)
                }
            }
        }
    }

}