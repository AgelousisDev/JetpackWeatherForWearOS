package com.agelousis.jetpackweatherwearos.mapAddressPicker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

typealias AddressDataModelSuccessBlock = (AddressDataModel?) -> Unit

@Parcelize
data class AddressDataModel(
    val countryName: String? = null,
    val countryCode: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val addressLine: String? = null
): Parcelable