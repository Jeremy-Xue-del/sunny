package com.example.sunny.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunny.databinding.TimeItemBinding
import com.example.sunny.model.Hourly
import com.example.sunny.model.getSky

class TimeAdapter(private val timeList: List<Hourly>) :
    RecyclerView.Adapter<TimeAdapter.ViewHolder>() {
    class ViewHolder(val binding: TimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TimeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeItem = timeList[position]
        holder.binding.apply {
            time.text = timeItem.time?.substring(11, 16)
            tuBiao.setImageResource(getSky(timeItem.code).icon)
            text.text = timeItem.text
            wenDu.text = "${timeItem.temperature}°"
        }
    }

    override fun getItemCount() = timeList.size
}
