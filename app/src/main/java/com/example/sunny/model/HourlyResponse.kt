package com.example.sunny.model

import com.google.gson.annotations.SerializedName

data class HourlyResponse(
    val results: List<HourlyResult>? = null
)

data class HourlyResult(
    val location: Location? = null,
    val hourly: List<Hourly>? = null
)

data class Hourly(
    val time: String? = null,
    val text: String? = null,
    val code: String? = null,
    val temperature: String? = null,
    val humidity: String? = null,
    @SerializedName("wind_direction")
    val windDirection: String? = null,
    @SerializedName("wind_speed")
    val windSpeed: String? = null,
)