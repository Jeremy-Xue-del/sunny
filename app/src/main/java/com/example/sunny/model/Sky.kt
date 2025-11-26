package com.example.sunny.model

import com.example.sunny.R

class Sky(val info: String, val icon: Int, val bg: Int)

private val sky = mapOf(
    "0" to Sky("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day),
    "1" to Sky("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night),
    "4" to Sky("多云", R.drawable.ic_partly_cloud_day, R.drawable.bg_partly_cloudy_day),
    "6" to Sky("多云", R.drawable.ic_partly_cloud_night, R.drawable.bg_partly_cloudy_night),
    "9" to Sky("阴", R.drawable.ic_cloudy, R.drawable.bg_cloudy),
    "33" to Sky("大风", R.drawable.ic_cloudy, R.drawable.bg_wind),
    "13" to Sky("小雨", R.drawable.ic_light_rain, R.drawable.bg_rain),
    "14" to Sky("中雨", R.drawable.ic_moderate_rain, R.drawable.bg_rain),
    "15" to Sky("大雨", R.drawable.ic_heavy_rain, R.drawable.bg_rain),
    "16" to Sky("暴雨", R.drawable.ic_storm_rain, R.drawable.bg_rain),
    "11" to Sky("雷阵雨", R.drawable.ic_thunder_shower, R.drawable.bg_rain),
    "20" to Sky("雨夹雪", R.drawable.ic_sleet, R.drawable.bg_rain),
    "22" to Sky("小雪", R.drawable.ic_light_snow, R.drawable.bg_snow),
    "23" to Sky("中雪", R.drawable.ic_moderate_snow, R.drawable.bg_snow),
    "24" to Sky("大雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "25" to Sky("暴雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "12" to Sky("冰雹", R.drawable.ic_hail, R.drawable.bg_snow),
    "27" to Sky("轻度雾霾", R.drawable.ic_light_haze, R.drawable.bg_fog),
    "28" to Sky("中度雾霾", R.drawable.ic_moderate_haze, R.drawable.bg_fog),
    "29" to Sky("重度雾霾", R.drawable.ic_heavy_haze, R.drawable.bg_fog),
    "30" to Sky("雾", R.drawable.ic_fog, R.drawable.bg_fog),
    "26" to Sky("浮尘", R.drawable.ic_fog, R.drawable.bg_fog)
)

fun getSky(code: String?): Sky {
    return sky[code] ?: sky["0"]!!
}