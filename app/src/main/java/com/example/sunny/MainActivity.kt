package com.example.sunny

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sunny.databinding.ActivityMainBinding
import com.example.sunny.model.WeatherResponse
import com.example.sunny.model.getSky
import com.example.sunny.service.ApiService
import com.example.sunny.util.SpUtils
import com.example.sunny.view.place.PlaceFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /// 位置权限码
    private val locationCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBarHeight()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val savedCity = SpUtils.getInstance().getString("city")
        if (savedCity.isNotEmpty()) {
            loadWeather(savedCity)
        } else {
            getLocationFromGPS()
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

    private fun loadWeather(city: String) {
        lifecycleScope.launch {
            val res = ApiService.getCurrentWeather(city)
            res.onSuccess { data ->
                upDataCity(data)
                SpUtils.getInstance().putString("city", city)
            }.onFailure { e ->
                toast("获取天气失败:${e.message}")
            }
        }
    }

    private fun upDataCity(data: WeatherResponse) {
        val now = data.results?.first()?.now
        val location = data.results?.first()?.location
        if (location != null) {
            val title = binding.root.findViewById<TextView>(R.id.placeName)
            title.text = location.name
        }
        if (now != null) {
            val currentTemp = binding.root.findViewById<TextView>(R.id.currentTemp)
            currentTemp.text = now.temperature ?: "--"
            val currentSky = binding.root.findViewById<TextView>(R.id.currentSky)
            currentSky.text = now.text ?: "--"
            
            val nowLayout = binding.root.findViewById<ConstraintLayout>(R.id.nowLayout)
            nowLayout.let{
                val sky = getSky(now.code)
                nowLayout.setBackgroundResource(sky.bg)
            }
        }

    }

    /**
     * GPS 定位（不使用回调，失败则跳转选择城市页）
     */
    private fun getLocationFromGPS() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->

            location?.let {
                val city = "${it.latitude},${it.longitude}"
                loadWeather(city)
            } ?: showPlaceSelectionInterface()

        }.addOnFailureListener {
            showPlaceSelectionInterface()
        }
    }

    private fun showPlaceSelectionInterface() {
        val placeFragment = PlaceFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.root.id, placeFragment)
            .commit()
    }

    /**
     * 权限判断
     */
    private fun hasLocationPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * 申请权限
     */
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            locationCode
        )
    }

    /**
     * 权限结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                getLocationFromGPS()
            } else {
                showPlaceSelectionInterface()
            }
        }
    }


    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
