package com.agelousis.jetpackweatherwearos.weather.ui

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agelousis.jetpackweatherwearos.network.repositories.SuccessUnitBlock
import com.agelousis.jetpackweatherwearos.ui.composableView.SimpleDialog
import com.agelousis.jetpackweatherwearos.ui.composableView.models.PositiveButtonBlock
import com.agelousis.jetpackweatherwearos.ui.composableView.models.SimpleDialogDataModel
import com.agelousis.jetpackweatherwearos.ui.theme.JetpackWeatherForWearOSTheme
import com.agelousis.jetpackweatherwearos.utils.extensions.arePermissionsGranted
import com.agelousis.jetpackweatherwearos.utils.helpers.LocationHelper
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.weather.viewModel.WeatherViewModel
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.agelousis.jetpackweatherwearos.R

@Composable
fun WeatherActivityLayout(
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current
    val loaderState by viewModel.loaderStateStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    var requestLocationOnStartupState by remember {
        mutableStateOf(value = true)
    }
    val addressDataModel by viewModel.addressDataModelStateFlow.collectAsState()
    requestLocation(
        context = context,
        scope = scope,
        state = requestLocationOnStartupState,
        viewModel = viewModel
    ) {
        requestLocationOnStartupState = false
    }

    if (viewModel.locationPermissionState
        || context.arePermissionsGranted(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        || addressDataModel != null
    )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (todayWeatherLayoutConstrainedReference, loaderConstrainedReference) =
                createRefs()

            TodayWeatherLayout(
                modifier = Modifier
                    .constrainAs(todayWeatherLayoutConstrainedReference) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .verticalScroll(
                        state = rememberScrollState()
                    ),
                viewModel = viewModel,
                refreshWeatherBlock = {
                    requestLocationOnStartupState = true
                }
            )
            if (loaderState)
                CircularProgressIndicator(
                    modifier = Modifier
                        .constrainAs(loaderConstrainedReference) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
        }
    else
        LocationPermissionRequest(
            viewModel =  viewModel
        )
}

@Composable
private fun LocationPermissionRequest(
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var locationPermissionDialogState by remember {
        mutableStateOf(value = false)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { locationPermissions ->
        viewModel.locationPermissionState = locationPermissions.all {
            it.value
        }
        locationPermissionDialogState = locationPermissions.none {
            it.value
        }
        requestLocation(
            context = context,
            scope = scope,
            viewModel = viewModel
        )
    }

    LocationPermissionDialog(
        state = locationPermissionDialogState,
        positiveButtonBlock = {
            locationPermissionDialogState = false
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        },
        dismissBlock = {
            locationPermissionDialogState = false
        }
    )

    LaunchedEffect(
        key1 = Unit
    ) {
        launch {
            delay(
                timeMillis = 1000
            )
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}

@Composable
private fun LocationPermissionDialog(
    state: Boolean,
    positiveButtonBlock: PositiveButtonBlock,
    dismissBlock: PositiveButtonBlock
) {
    SimpleDialog(
        show = state,
        simpleDialogDataModel = SimpleDialogDataModel(
            title = stringResource(id = R.string.key_warning_label),
            message = stringResource(id = R.string.key_location_permission_approval_message),
            positiveButtonBlock = positiveButtonBlock,
            dismissBlock = dismissBlock
        )
    )
}

private fun requestLocation(
    context: Context,
    scope: CoroutineScope,
    state: Boolean = true,
    fromUpdate: Boolean = false,
    viewModel: WeatherViewModel,
    successUnitBlock: SuccessUnitBlock = {}
) {
    val preferencesStoreHelper = PreferencesStoreHelper(
        context = context
    )
    if (context.arePermissionsGranted(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        && state
    ) {
        if (!fromUpdate)
            viewModel.requestLocationMutableState.value = true
        LocationHelper(
            context = context,
            priority = Priority.PRIORITY_HIGH_ACCURACY
        ) { location ->
            if (!fromUpdate)
                viewModel.requestLocationMutableState.value = false
            LocationHelper.getAddressFromLocation(
                context = context,
                longitude = location.longitude,
                latitude = location.latitude
            ) { addressDataModel ->
                viewModel.addressDataModelMutableStateFlow.value = addressDataModel

                viewModel.weatherUiAppBarTitle = addressDataModel?.addressLine
                scope.launch {
                    preferencesStoreHelper setCurrentAddressData addressDataModel
                }
                requestWeather(
                    context = context,
                    viewModel = viewModel,
                    longitude = location.longitude,
                    latitude = location.latitude
                )
                successUnitBlock()
            }
        }
    }
}


fun requestWeather(
    context: Context,
    viewModel: WeatherViewModel,
    longitude: Double,
    latitude: Double
) {
    viewModel.requestForecast(
        context = context,
        location = "%s,%s".format(
            latitude,
            longitude
        ),
        days = 7,
        airQualityState = true,
        alertsState = true
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    JetpackWeatherForWearOSTheme {
        WeatherActivityLayout(
            viewModel = viewModel()
        )
    }
}
