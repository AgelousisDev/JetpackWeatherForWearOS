package com.agelousis.jetpackweatherwearos.utils.helpers

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModel
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModelSuccessBlock
import com.agelousis.jetpackweatherwearos.utils.extensions.arePermissionsGranted
import com.agelousis.jetpackweatherwearos.utils.extensions.isAndroid13
import com.google.android.gms.location.*
import java.util.*

typealias LocationSuccessBlock = (Location) -> Unit
typealias LocationPermissionsDeclinedBlock = () -> Unit

class LocationHelper(
        private val context: Context,
        private val removeLocationUpdates: Boolean = true,
        private val interval: Long? = null,
        private val priority: Int? = null,
        private val smallestDisplacement: Float = 100f,
        locationPermissionsDeclinedBlock: LocationPermissionsDeclinedBlock? = null,
        private val locationSuccessBlock: LocationSuccessBlock
): LocationCallback() {

    companion object {

        fun getAddressFromLocation(
            context: Context,
            longitude: Double,
            latitude: Double,
            addressDataModelSuccessBlock: AddressDataModelSuccessBlock
        ) {
            Geocoder(context, Locale.ENGLISH).apply {
                try {
                    if (isAndroid13)
                        getFromLocation(
                            latitude,
                            longitude,
                            1
                        ) { addresses ->
                            addresses.takeIf {
                                it.isNotEmpty()
                            } ?: return@getFromLocation
                            addressDataModelSuccessBlock(
                                this@Companion getAddressDataFrom addresses.firstOrNull()
                            )
                        }
                    else
                        getFromLocation(latitude, longitude, 1)?.let { addresses ->
                            addresses.takeIf {
                                it.isNotEmpty()
                            } ?: return@let
                            addressDataModelSuccessBlock(
                                this@Companion getAddressDataFrom addresses.firstOrNull()
                            )
                        }
                } catch (e: Exception) {
                    addressDataModelSuccessBlock(
                        null
                    )
                }
            }
        }

        fun getLocationFromAddress(
            context: Context,
            strAddress: String,
            addressDataModelSuccessBlock: AddressDataModelSuccessBlock
        ) =
            with(Geocoder(context, Locale.ENGLISH)) {
                if (isAndroid13)
                    getFromLocationName(
                        strAddress,
                        1
                    ) { addresses ->
                        addresses.takeIf {
                            it.isNotEmpty()
                        } ?: return@getFromLocationName
                        addressDataModelSuccessBlock(
                            AddressDataModel(
                                countryName = addresses.firstOrNull()?.countryName,
                                countryCode = addresses.firstOrNull()?.countryCode,
                                longitude = addresses.firstOrNull()?.longitude,
                                latitude = addresses.firstOrNull()?.latitude,
                                addressLine = addresses.firstOrNull()?.getAddressLine(0)
                            )
                        )
                    }
                else
                    getFromLocationName(strAddress, 1)?.let { addresses ->
                        addresses.takeIf {
                            it.isNotEmpty()
                        } ?: return@with null
                        addressDataModelSuccessBlock(
                            AddressDataModel(
                                countryName = addresses.firstOrNull()?.countryName,
                                countryCode = addresses.firstOrNull()?.countryCode,
                                longitude = addresses.firstOrNull()?.longitude,
                                latitude = addresses.firstOrNull()?.latitude,
                                addressLine = addresses.firstOrNull()?.getAddressLine(0)
                            )
                        )
                    }
            }

        private infix fun getAddressDataFrom(address: Address?) =
            if (address != null)
                AddressDataModel(
                    countryName = address.countryName,
                    countryCode = address.countryCode,
                    longitude = address.longitude,
                    latitude = address.latitude,
                    addressLine = address.getAddressLine(0)?.takeIf { addressLine ->
                        addressLine.split(",").size < 3
                    } ?: listOf(
                        with(address.getAddressLine(0)?.split(",")?.lastIndex ?: 0) {
                            address.getAddressLine(0)?.split(",")?.getOrNull(
                                index = this - 1
                            )
                        },
                        address.getAddressLine(0)?.split(",")?.lastOrNull() ?: ""
                    ).joinToString()
                )
            else
                null

    }

    override fun onLocationResult(p0: LocationResult) {
        super.onLocationResult(p0)
        locationSuccessBlock(p0.lastLocation ?: return)
        if (removeLocationUpdates)
            LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(this)
    }

    private val locationRequest by lazy {
        LocationRequest.Builder(
            priority ?: Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            interval ?: (10L * 1000)
        )
            .setMinUpdateDistanceMeters(smallestDisplacement)
            .build()
    }

    init {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)
        if (context.arePermissionsGranted(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(
                locationRequest,
                this,
                Looper.getMainLooper()
            )
        else
            locationPermissionsDeclinedBlock?.invoke()
    }

}