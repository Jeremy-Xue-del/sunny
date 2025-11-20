package com.example.sunny.view.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sunny.databinding.ActivityWeatherBinding

class Weather : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}