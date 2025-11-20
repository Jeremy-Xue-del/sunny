package com.example.sunny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sunny.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBarHeight()


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

}
