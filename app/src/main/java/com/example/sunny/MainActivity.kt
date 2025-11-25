package com.example.sunny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sunny.databinding.ActivityMainBinding
import com.example.sunny.service.ApiService
import com.example.sunny.util.SpUtils
import com.example.sunny.view.place.PlaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBarHeight()
        val savedCity = SpUtils.getInstance().getString("city")
        if (savedCity.isNotEmpty()) {
            showWeatherInterface(savedCity)
        } else {
            showPlaceSelectionInterface()
        }

    }

    private fun setupStatusBarHeight() {
        val titleLayout = binding.root.findViewById<ConstraintLayout>(R.id.title)
        ViewCompat.setOnApplyWindowInsetsListener(titleLayout) { _, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val layoutParams = titleLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = statusBarInsets.top
            insets
        }
    }

    private fun showWeatherInterface(city: String) {
        // TODO: 实现显示天气界面的逻辑
        ApiService.getCurrentWeather(city, callback =  {})
    }

    private fun showPlaceSelectionInterface() {
        val placeFragment = PlaceFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.root.id, placeFragment)
            .commit()
    }

}
