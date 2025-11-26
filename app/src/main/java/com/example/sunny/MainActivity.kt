package com.example.sunny

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sunny.databinding.ActivityMainBinding
import com.example.sunny.databinding.ForecastItemBinding
import com.example.sunny.databinding.TimeItemBinding
import com.example.sunny.model.DailyResponse
import com.example.sunny.model.HourlyResponse
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
        val titleLayout = binding.nowInclude.title
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
                loadHourlyWeather(city)
                loadDailyWeather(city)
                SpUtils.getInstance().putString("city", city)
            }.onFailure { e ->
                toast("获取天气失败:${e.message}")
            }
        }
    }

    private fun loadHourlyWeather(city: String) {
        lifecycleScope.launch {
            val res = ApiService.getHourlyWeather(city)
            res.onSuccess { data ->
                upDataHourly(data)
            }.onFailure { e ->
                toast("获取天气失败:${e.message}")
            }
        }
    }

    private fun loadDailyWeather(city: String) {
        lifecycleScope.launch {
            val res = ApiService.getDailyWeather(city)
            res.onSuccess { data ->
                upDataDaily(data)
            }.onFailure { e ->
                toast("获取天气失败:${e.message}")
            }
        }
    }

    private fun upDataCity(data: WeatherResponse) {
        val now = data.results?.first()?.now
        val location = data.results?.first()?.location
        if (location != null) {
            val title = binding.nowInclude.placeName
            title.text = location.name
        }
        if (now != null) {

            val currentTemp = binding.nowInclude.currentTemp
            currentTemp.text = now.temperature ?: "--"
            val currentSky = binding.nowInclude.currentSky
            currentSky.text = now.text ?: "--"
            val currentApparent = binding.nowInclude.currentApparent
            currentApparent.text = now.feelsLike ?: "--"
            val currentPressure = binding.nowInclude.currentPressure
            currentPressure.text = now.pressure ?: "--"

            val currentHumidity = binding.nowInclude.currentHumidity
            currentHumidity.text = now.humidity ?: "--"
            val nowLayout = binding.nowInclude.nowLayout
            nowLayout.let {
                val sky = getSky(now.code)
                nowLayout.setBackgroundResource(sky.bg)
            }
        }

    }

    private fun upDataHourly(data: HourlyResponse) {
        val hourlies = data.results?.first()?.hourly
        val timeLayout = binding.timeInclude.timeLayout
        if (hourlies != null) {
            timeLayout.removeAllViews()
            for (hourly in hourlies) {
                val timeItem = TimeItemBinding.inflate(layoutInflater)
                timeItem.time.text = hourly.time?.substring(11, 16) ?: "--:--"
                timeItem.text.text = hourly.text ?: "--"
                timeItem.wenDu.text = hourly.temperature ?: "--"
                val sky = getSky(hourly.code)
                timeItem.tuBiao.setImageResource(sky.icon)
                timeLayout.addView(timeItem.root)
            }
        }
    }

    private fun upDataDaily(data: DailyResponse) {
        val dailies = data.results?.first()?.daily
        val dailyLayout = binding.forecastInclude.forecastLayout
        if (dailies != null) {
            dailyLayout.removeAllViews()
            for (daily in dailies) {
                val dailyItem = ForecastItemBinding.inflate(layoutInflater)
                dailyItem.dateInfo.text = daily.date ?: "xx-xx"
                dailyItem.skyInfo.text = "${daily.textDay ?: "--"}/${daily.textNight ?: "--"}"
                dailyItem.temperatureInfo.text = "${daily.low ?: "--"}/${daily.high ?: "--"}"
                val sky = getSky(daily.codeDay)
                dailyItem.skyIcon.setImageResource(sky.icon)
                dailyLayout.addView(dailyItem.root)
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

        try {
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
        } catch (_: SecurityException) {
            // 处理安全异常，比如显示选择界面
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
