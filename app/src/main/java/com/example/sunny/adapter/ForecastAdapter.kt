package com.example.sunny.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunny.databinding.ForecastItemBinding
import com.example.sunny.model.Daily
import com.example.sunny.model.getSky

class ForecastAdapter(private val forecastList: List<Daily>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    class ViewHolder(val binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = forecastList[position]
        holder.binding.apply {
            // 设置数据到视图上
            dateInfo.text = daily.date ?: "xx-xx"
            skyInfo.text = daily.textDay ?: "--"
            temperatureInfo.text = "${daily.low ?: "--"}~${daily.high ?: "--"}℃"
            skyIcon.setImageResource(getSky(daily.codeDay).icon)
        }
    }

    override fun getItemCount() = forecastList.size
}