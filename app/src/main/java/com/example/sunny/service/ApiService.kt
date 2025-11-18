package com.example.sunny.service

import com.example.sunny.model.CitySearchResponse
import com.example.sunny.model.WeatherResponse
import okhttp3.OkHttpClient
object ApiService {
    private lateinit var weatherService: WeatherApiService

    /**
     * 初始化 ApiService
     */
    fun initialize(okHttpClient: OkHttpClient, apiKey: String) {
        this.weatherService = WeatherApiService(okHttpClient, apiKey)
    }

    // 代理方法
    fun searchCity(query: String, callback: (Result<CitySearchResponse>) -> Unit) {
        weatherService.searchCity(query, callback)
    }

    fun getCurrentWeather(
        location: String,
        language: String = "zh-Hans",
        unit: String = "c",
        callback: (Result<WeatherResponse>) -> Unit
    ) {
        weatherService.getCurrentWeather(location, language, unit, callback)
    }
}
