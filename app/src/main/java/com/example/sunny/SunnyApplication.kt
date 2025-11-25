package com.example.sunny

import android.app.Application
import android.content.Context
import com.example.sunny.service.ApiService
import com.example.sunny.util.SpUtils
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class SunnyApplication : Application() {
    companion object {
        const val TOKEN = "SWavoyrHoqYLY55Fk"
        lateinit var context: Context
        lateinit var okHttpClient: OkHttpClient
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        // 初始化 OkHttpClient
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        ApiService.initialize(okHttpClient, TOKEN)

        SpUtils.getInstance().initialize(this)
    }
}