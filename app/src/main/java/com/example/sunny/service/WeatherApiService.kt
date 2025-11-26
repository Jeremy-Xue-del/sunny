package com.example.sunny.service

import com.example.sunny.model.CitySearchResponse
import com.example.sunny.model.HourlyResponse
import com.example.sunny.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class WeatherApiService(
    private val client: OkHttpClient,
    private val apiKey: String
) {

    private val gson = Gson()

    /**
     * 搜索城市（返回 Result）
     */
    suspend fun searchCity(query: String): Result<CitySearchResponse> =
        request(
            endpoint = "location/search.json",
            params = mapOf("q" to query),
            classOfT = CitySearchResponse::class.java
        )

    /**
     * 获取实时天气（返回 Result）
     */
    suspend fun getCurrentWeather(
        location: String,
    ): Result<WeatherResponse> =
        request(
            endpoint = "weather/now.json",
            params = mapOf(
                "location" to location,
                "language" to "zh-Hans",
                "unit" to "c"
            ),
            classOfT = WeatherResponse::class.java
        )

    suspend fun getHourlyWeather(
        location: String,
    ): Result<HourlyResponse> =
        request(
            endpoint = "weather/hourly.json",
            params = mapOf(
                "location" to location,
                "language" to "zh-Hans",
                "unit" to "c",
                "start" to "0",
                "hours" to "24"
            ),
            classOfT = HourlyResponse::class.java
        )


    /**
     * 通用请求方法（统一变协程 + Result）
     */
    private suspend fun <T> request(
        endpoint: String,
        params: Map<String, String>,
        classOfT: Class<T>
    ): Result<T> = withContext(Dispatchers.IO) {
        try {
            val urlBuilder = buildWeatherUrl(*endpoint.split("/").toTypedArray())
                .addQueryParameter("key", apiKey)

            params.forEach { (k, v) ->
                urlBuilder.addQueryParameter(k, v)
            }

            val request = Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    IOException("Request failed: HTTP ${response.code}")
                )
            }

            if (body.isNullOrEmpty()) {
                return@withContext Result.failure(
                    IOException("Empty response body")
                )
            }

            return@withContext try {
                val result = gson.fromJson(body, classOfT)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(IOException("JSON parse error: ${e.message}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * 统一构建 URL
     */
    private fun buildWeatherUrl(vararg paths: String): HttpUrl.Builder {
        val builder = HttpUrl.Builder()
            .scheme("https")
            .host("api.seniverse.com")
            .addPathSegment("v3")

        paths.forEach { p ->
            p.split("/").filter { it.isNotEmpty() }.forEach { seg ->
                builder.addPathSegment(seg)
            }
        }

        return builder
    }
}
