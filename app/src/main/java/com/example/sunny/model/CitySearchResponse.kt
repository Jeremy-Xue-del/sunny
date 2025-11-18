package com.example.sunny.model

import com.google.gson.annotations.SerializedName

data class CitySearchResponse(
    val results: List<CityResult>? = null
)

data class CityResult(
    val id: String? = null,
    val name: String? = null,
    val country: String? = null,
    val path: String? = null,
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezoneOffset: String? = null
)
