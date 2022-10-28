package com.agelousis.jetpackweatherwearos.network.repositories

import com.agelousis.jetpackweatherwearos.network.NetworkHelper
import com.agelousis.jetpackweatherwearos.network.apis.WeatherAPI
import com.agelousis.jetpackweatherwearos.network.response.ErrorModel
import com.agelousis.jetpackweatherwearos.network.response.WeatherResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias SuccessUnitBlock = () -> Unit
typealias SuccessBlock<T> = (T) -> Unit
typealias FailureBlock = (ErrorModel) -> Unit

object WeatherRepository {

    /**
     * @param location
     * @param airQualityState
     */
    fun requestCurrentWeather(
        location: String,
        airQualityState: Boolean,
        successBlock: SuccessBlock<WeatherResponseModel>,
        failureBlock: FailureBlock
    ) {
        NetworkHelper.create<WeatherAPI>()?.requestCurrentWeather(
            location = location,
            airQualityState = if (airQualityState) "yes" else "no"
        )?.enqueue(
            object: Callback<WeatherResponseModel> {
                override fun onResponse(
                    call: Call<WeatherResponseModel>,
                    response: Response<WeatherResponseModel>
                ) {
                    successBlock(response.body() ?: return)
                }

                override fun onFailure(call: Call<WeatherResponseModel>, t: Throwable) {
                    failureBlock(
                        ErrorModel(
                            localizedMessage = t.localizedMessage
                        )
                    )
                }
            }
        )
    }

    /**
     * @param location
     * @param days
     * @param airQualityState
     * @param alertsState
     */
    fun requestForecast(
        location: String,
        days: Int,
        airQualityState: Boolean,
        alertsState: Boolean,
        successBlock: SuccessBlock<WeatherResponseModel>,
        failureBlock: FailureBlock
    ) {
        NetworkHelper.create<WeatherAPI>()?.requestForecast(
            location = location,
            days = days,
            airQualityState = if (airQualityState) "yes" else "no",
            alertsState = if (alertsState) "yes" else "no"
        )?.enqueue(
            object: Callback<WeatherResponseModel> {
                override fun onResponse(
                    call: Call<WeatherResponseModel>,
                    response: Response<WeatherResponseModel>
                ) {
                    successBlock(response.body() ?: return)
                }

                override fun onFailure(call: Call<WeatherResponseModel>, t: Throwable) {
                    failureBlock(
                        ErrorModel(
                            localizedMessage = t.localizedMessage
                        )
                    )
                }
            }
        )
    }

}