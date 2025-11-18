package com.example.sunny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sunny.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragment会自动加载，无需额外设置
    }
}
