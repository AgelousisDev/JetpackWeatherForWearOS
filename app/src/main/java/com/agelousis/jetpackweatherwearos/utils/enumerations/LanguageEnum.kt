package com.agelousis.jetpackweatherwearos.utils.enumerations

import android.content.Context
import com.agelousis.jetpackweatherwearos.R

enum class LanguageEnum(val locale: String) {
    ENGLISH(locale = "en-GB"),
    GREEK(locale = "el-GR");

    companion object {

        private const val COUNTRY_FLAG_URL = "https://flagpedia.net/data/flags/normal/%s.png"

        infix fun languagesFrom(
            context: Context
        ): Array<String> = context.resources.getStringArray(R.array.key_languages_array)

    }

    infix fun labelFrom(
        context: Context
    ): String = context.resources.getStringArray(
        R.array.key_languages_array
    )[ordinal]

    private val countryCode
        get() = when(this) {
            ENGLISH -> "GB"
            GREEK -> "GR"
        }

    val iconUrl
        get() = String.format(
            COUNTRY_FLAG_URL,
            countryCode.lowercase()
        )

}