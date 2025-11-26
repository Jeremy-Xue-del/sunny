package com.example.sunny.service

import com.example.sunny.model.CitySearchResponse
import com.example.sunny.model.DailyResponse
import com.example.sunny.model.HourlyResponse
import com.example.sunny.model.WeatherResponse
import okhttp3.OkHttpClient

object ApiService {

    private lateinit var weatherService: WeatherApiService

    /**
     * 初始化
     */
    fun initialize(
        okHttpClient: OkHttpClient,
        apiKey: String
    ) {
        weatherService = WeatherApiService(okHttpClient, apiKey)
    }

    /**
     * 搜索城市
     */
    suspend fun searchCity(query: String): Result<CitySearchResponse> {
        return weatherService.searchCity(query)
    }

    /**
     * 获取实时天气
     */
    suspend fun getCurrentWeather(city: String): Result<WeatherResponse> {
        return weatherService.getCurrentWeather(city)
    }

    /**
     * 获取逐小时天气
     */
    suspend fun getHourlyWeather(city: String): Result<HourlyResponse> {
        return weatherService.getHourlyWeather(city)
    }

    /**
     * 获取逐天天气
     */
    suspend fun getDailyWeather(city: String): Result<DailyResponse> {
        return weatherService.getDailyWeather(city)
    }
}
