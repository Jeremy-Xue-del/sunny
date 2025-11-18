package com.example.sunny.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val results: List<WeatherResult>? = null
)

data class WeatherResult(
    val location: Location? = null,
    val now: Now? = null,
    @SerializedName("last_update")
    val lastUpdate: String? = null
)

data class Location(
    val id: String? = null,
    val name: String? = null,
    val country: String? = null,
    val path: String? = null,
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezoneOffset: String? = null
)

data class Now(
    val text: String? = null,
    val code: String? = null,
    val temperature: String? = null,
    @SerializedName("feels_like")
    val feelsLike: String? = null,
    val pressure: String? = null,
    val humidity: String? = null,
    val visibility: String? = null,
    @SerializedName("wind_direction")
    val windDirection: String? = null,
    @SerializedName("wind_direction_degree")
    val windDirectionDegree: String? = null,
    @SerializedName("wind_speed")
    val windSpeed: String? = null,
    @SerializedName("wind_scale")
    val windScale: String? = null,
    val clouds: String? = null,
    @SerializedName("dew_point")
    val dewPoint: String? = null
)
