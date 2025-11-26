package com.example.sunny.model

import com.google.gson.annotations.SerializedName

data class DailyResponse(
    val results: List<DailyResult>? = null
)

data class DailyResult(
    val location: Location? = null,
    val daily: List<Daily>? = null,
    @SerializedName("last_update")
    val lastUpdate: String? = null
)

data class Daily(
    val date: String? = null,
    @SerializedName("text_day")
    val textDay: String? = null,
    @SerializedName("code_day")
    val codeDay: String? = null,
    @SerializedName("text_night")
    val textNight: String? = null,
    @SerializedName("code_night")
    val codeNight: String? = null,
    val high: String? = null,
    val low: String? = null,
    val precip: String? = null,
    @SerializedName("wind_direction")
    val windDirection: String? = null,
    @SerializedName("wind_direction_degree")
    val windDirectionDegree: String? = null,
    @SerializedName("wind_speed")
    val windSpeed: String? = null,
    @SerializedName("wind_scale")
    val windScale: String? = null,
    val rainfall: String? = null,
    val humidity: String? = null,
)