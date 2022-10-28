package com.agelousis.jetpackweatherwearos.weather.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.mapAddressPicker.model.AddressDataModel
import com.agelousis.jetpackweatherwearos.network.repositories.SuccessBlock
import com.agelousis.jetpackweatherwearos.network.repositories.WeatherRepository
import com.agelousis.jetpackweatherwearos.network.response.WeatherResponseModel
import com.agelousis.jetpackweatherwearos.ui.models.HeaderModel
import com.agelousis.jetpackweatherwearos.utils.constants.Constants
import com.agelousis.jetpackweatherwearos.utils.extensions.toDate
import com.agelousis.jetpackweatherwearos.utils.extensions.toDisplayDate
import com.agelousis.jetpackweatherwearos.utils.helpers.PreferencesStoreHelper
import com.agelousis.jetpackweatherwearos.weather.bottomNavigation.WeatherNavigationScreen
import com.agelousis.jetpackweatherwearos.weather.model.WeatherSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val uiScope = CoroutineScope(context = Dispatchers.Main)

    var weatherUiAppBarTitle by mutableStateOf<String?>(
        value = null
    )
    val bottomNavigationItems = mutableStateListOf(
        WeatherNavigationScreen.Today,
        WeatherNavigationScreen.Tomorrow,
        WeatherNavigationScreen.NextDays
    )
    var currentNavigationRoute by mutableStateOf(
        value = WeatherNavigationScreen.Today.route
    )

    private val loaderStateMutableStateFlow = MutableStateFlow(value = false)
    val loaderStateStateFlow: StateFlow<Boolean> = loaderStateMutableStateFlow.asStateFlow()

    private val networkErrorMutableStateFlow = MutableStateFlow(value = false)
    val networkErrorStateFlow: StateFlow<Boolean> = networkErrorMutableStateFlow.asStateFlow()

    private val _showDialog = MutableStateFlow(value = false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    val requestLocationMutableState = MutableStateFlow(value = false)
    val requestLocationState: StateFlow<Boolean> = requestLocationMutableState.asStateFlow()

    var alertPair by mutableStateOf<Pair<String?, String?>>(value = null to null)

    private fun showDialog() {
        _showDialog.value = true
    }

    val swipeRefreshMutableStateFlow = MutableStateFlow(value = false)
    val swipeRefreshStateFlow: StateFlow<Boolean> = swipeRefreshMutableStateFlow.asStateFlow()

    val lazyColumnFirstChildVisibilityMutableStateFlow = MutableStateFlow(value = true)
    val lazyColumnFirstChildVisibilityStateFlow = lazyColumnFirstChildVisibilityMutableStateFlow.asStateFlow()

    fun onDialogConfirm() {
        alertPair = null to null
        _showDialog.value = false
        // Continue with executing the confirmed action
    }

    fun onDialogDismiss() {
        alertPair = null to null
        _showDialog.value = false
    }

    var locationPermissionState by mutableStateOf(
        value = false
    )
    val addressDataModelMutableStateFlow = MutableStateFlow<AddressDataModel?>(
        value = null
    )
    val addressDataModelStateFlow = addressDataModelMutableStateFlow.asStateFlow()

    private val weatherResponseMutableLiveData by lazy { MutableLiveData<WeatherResponseModel>() }
    val weatherResponseLiveData: LiveData<WeatherResponseModel>
        get() = weatherResponseMutableLiveData

    val weatherSettingsList = mutableStateListOf<WeatherSettings>()

    val nextDaysForecastDataList: List<Any>
        get() {
            val items = mutableListOf<Any>()
            for (weatherForecastDayDataModel in (weatherResponseLiveData.value?.weatherForecastDataModel?.nextWeatherForecastDayDataModelList ?: listOf())) {
                if (weatherForecastDayDataModel?.weatherHourlyDataModelList == null)
                    continue
                items.add(
                    HeaderModel(
                        header = weatherForecastDayDataModel.date?.toDate(
                            pattern = Constants.SERVER_DATE_FORMAT
                        )?.toDisplayDate(
                            pattern = Constants.DISPLAY_DATE_FORMAT
                        ) ?: ""
                    )
                )
                items.add(
                    weatherForecastDayDataModel.currentDayWeatherDataModel ?: continue
                )
                items.add(
                    weatherForecastDayDataModel.weatherHourlyDataModelList
                )
            }
            return items
        }

    fun requestCurrentWeather(
        context: Context,
        location: String,
        airQualityState: Boolean,
        successBlock: SuccessBlock<WeatherResponseModel>? = null
    ) {
        loaderStateMutableStateFlow.value = true
        WeatherRepository.requestCurrentWeather(
            location = location,
            airQualityState = airQualityState,
            successBlock = { weatherResponseModel ->
                swipeRefreshMutableStateFlow.value = false
                loaderStateMutableStateFlow.value = false
                networkErrorMutableStateFlow.value = false
                weatherResponseMutableLiveData.value = weatherResponseModel
                //weatherUiAppBarTitle = weatherResponseModel.weatherLocationDataModel?.regionCountry
                successBlock?.invoke(weatherResponseModel)
            },
            failureBlock = {
                swipeRefreshMutableStateFlow.value = false
                loaderStateMutableStateFlow.value = false
                networkErrorMutableStateFlow.value = true
                alertPair = context.resources.getString(R.string.key_error_label) to it.localizedMessage
                showDialog()
            }
        )
    }

    fun requestForecast(
        context: Context,
        navController: NavController,
        location: String,
        days: Int,
        airQualityState: Boolean,
        alertsState: Boolean
    ) {
        val preferencesStoreHelper = PreferencesStoreHelper(
            context = context
        )
        loaderStateMutableStateFlow.value = !swipeRefreshStateFlow.value
        WeatherRepository.requestForecast(
            location = location,
            days = days,
            airQualityState = airQualityState,
            alertsState = alertsState,
            successBlock = { weatherResponseModel ->
                swipeRefreshMutableStateFlow.value = false
                loaderStateMutableStateFlow.value = false
                networkErrorMutableStateFlow.value = false
                weatherResponseMutableLiveData.value = weatherResponseModel
                this configureBottomNavigationItemsWith navController
                //weatherUiAppBarTitle = weatherResponseModel.weatherLocationDataModel?.regionCountry
                uiScope.launch {
                    preferencesStoreHelper setWeatherResponseModelData weatherResponseModel
                }
            },
            failureBlock = {
                swipeRefreshMutableStateFlow.value = false
                loaderStateMutableStateFlow.value = false
                uiScope.launch {
                    if (preferencesStoreHelper.offlineMode.firstOrNull() == true
                        && preferencesStoreHelper.weatherResponseModelData.firstOrNull() != null
                    ) {
                        weatherResponseMutableLiveData.value = preferencesStoreHelper.weatherResponseModelData.firstOrNull()
                        weatherUiAppBarTitle = preferencesStoreHelper.weatherResponseModelData.firstOrNull()?.weatherLocationDataModel?.regionCountry
                    }
                    else
                        networkErrorMutableStateFlow.value = true
                }
                //alertPair = context.resources.getString(R.string.key_error_label) to it.localizedMessage
                //onOpenDialogClicked()
            }
        )
    }

    private infix fun configureBottomNavigationItemsWith(
        navController: NavController
    ) {
        if (!weatherResponseLiveData.value?.weatherAlertsDataModel?.weatherAlertsModelList.isNullOrEmpty()) {
            if (WeatherNavigationScreen.Alerts !in bottomNavigationItems)
                bottomNavigationItems.add(
                    WeatherNavigationScreen.Alerts
                )
        }
        else {
            bottomNavigationItems.remove(
                WeatherNavigationScreen.Alerts
            )
            if ((WeatherNavigationScreen fromRoute currentNavigationRoute) is WeatherNavigationScreen.Alerts)
                navController.navigate(
                    WeatherNavigationScreen.Today.route
                )
        }
    }

}