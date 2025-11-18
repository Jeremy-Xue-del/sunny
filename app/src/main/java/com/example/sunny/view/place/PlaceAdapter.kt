// PlaceAdapter.kt
package com.example.sunny.view.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunny.databinding.PlaceItemBinding
import com.example.sunny.model.CityResult

class PlaceAdapter(private val cityList: List<CityResult>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder(val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList[position]
        holder.binding.apply {
            placeName.text = city.name ?: "未知城市"
            placeAddress.text = city.path ?: "${city.country ?: ""} ${city.timezone ?: ""}"
        }
    }

    override fun getItemCount() = cityList.size
}
