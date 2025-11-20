package com.example.sunny.view.now

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.sunny.databinding.ActivityNowBinding

class Now : AppCompatActivity() {

    lateinit var binding: ActivityNowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNowBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}