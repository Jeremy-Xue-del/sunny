// WeatherApiService.kt
package com.example.sunny.service

import com.example.sunny.model.CitySearchResponse
import com.example.sunny.model.WeatherResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.Response
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class WeatherApiService(private val client: OkHttpClient, private val apiKey: String) {
    private val gson = Gson()

    /**
     * 搜索城市信息
     */
    fun searchCity(query: String, callback: (Result<CitySearchResponse>) -> Unit) {
        makeGetRequest(
            "location/search.json",
            mapOf("q" to query),
            CitySearchResponse::class.java,
            callback
        )
    }

    /**
     * 获取当前天气
     */
    fun getCurrentWeather(
        location: String,
        language: String = "zh-Hans",
        unit: String = "c",
        callback: (Result<WeatherResponse>) -> Unit
    ) {
        makeGetRequest(
            "weather/now.json",
            mapOf(
                "location" to location,
                "language" to language,
                "unit" to unit
            ),
            WeatherResponse::class.java,
            callback
        )
    }

    /**
     * 通用GET请求方法
     */
    private inline fun <reified T> makeGetRequest(
        endpoint: String,
        parameters: Map<String, String>,
        responseType: Class<T>,
        crossinline callback: (Result<T>) -> Unit
    ) {
        val urlBuilder = buildWeatherUrl(*endpoint.split("/").toTypedArray())
            .addQueryParameter("key", apiKey)

        parameters.forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, value)
        }

        val request = Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build()

        client.newCall(request).enqueue(createCallback(responseType, callback))
    }

    /**
     * 创建统一回调处理器
     */
    private inline fun <reified T> createCallback(responseType: Class<T>, crossinline callback: (Result<T>) -> Unit): Callback {
        return object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, responseType, callback)
            }
        }
    }

    /**
     * 构建天气API基础URL
     */
    private fun buildWeatherUrl(vararg paths: String): HttpUrl.Builder {
        val builder = HttpUrl.Builder()
            .scheme("https")
            .host("api.seniverse.com")
            .addPathSegment("v3")

        paths.forEach { path ->
            // 处理包含"/"的路径段
            path.split("/").filter { it.isNotEmpty() }.forEach { segment ->
                builder.addPathSegment(segment)
            }
        }

        return builder
    }

    /**
     * 统一处理响应结果
     */
    private inline fun <reified T> handleResponse(
        response: Response,
        clazz: Class<T>,
        callback: (Result<T>) -> Unit
    ) {
        response.body?.string()?.let { responseBody ->
            if (response.isSuccessful) {
                try {
                    val result = gson.fromJson(responseBody, clazz)
                    callback(Result.success(result))
                } catch (e: Exception) {
                    callback(Result.failure(IOException("JSON parse error: ${e.message}")))
                }
            } else {
                callback(Result.failure(IOException("Request failed with code: ${response.code}")))
            }
        } ?: callback(Result.failure(IOException("Empty response body")))
    }
}
