package com.example.sunny

import android.app.Application
import android.content.Context

class SunnyApplication: Application() {
    companion object {
        const val TOKEN = "9Mf6NfpCHgUp2W1E"
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}