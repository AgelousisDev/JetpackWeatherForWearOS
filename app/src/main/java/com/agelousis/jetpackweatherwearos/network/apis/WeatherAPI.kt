package com.agelousis.jetpackweatherwearos.network.apis

import com.agelousis.jetpackweatherwearos.BuildConfig
import com.agelousis.jetpackweatherwearos.network.response.WeatherResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET(value = "current.json")
    fun requestCurrentWeather(
        @Query(value = "key") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query(value = "q") location: String,
        @Query(value = "aqi") airQualityState: String = "no",
        //@Query(value = "lang") lang: String = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
    ): Call<WeatherResponseModel>

    @GET(value = "forecast.json")
    fun requestForecast(
        @Query(value = "key") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query(value = "q") location: String,
        @Query(value = "days") days: Int = 1,
        @Query(value = "aqi") airQualityState: String = "no",
        @Query(value = "alerts") alertsState: String = "no",
        //@Query(value = "lang") lang: String = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
    ): Call<WeatherResponseModel>


}