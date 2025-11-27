package com.example.sunny.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunny.databinding.PlaceItemBinding
import com.example.sunny.model.CityResult

class PlaceAdapter(
    private val cityList: MutableList<CityResult>
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder(val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root)

    // 添加点击监听器字段
    private var itemClickListener: ((CityResult) -> Unit)? = null
    
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
            placeTimezone.text = city.timezone ?: ""

            // 添加点击事件监听
            root.setOnClickListener {
                itemClickListener?.invoke(city)
            }
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun updateData(newList: List<CityResult>) {
        cityList.clear()
        cityList.addAll(newList)
    }

    // 添加设置点击监听器的方法
    fun setOnItemClickListener(listener: (CityResult) -> Unit) {
        itemClickListener = listener
    }
}