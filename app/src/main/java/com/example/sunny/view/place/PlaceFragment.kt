package com.example.sunny.view.place

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunny.adapter.PlaceAdapter
import com.example.sunny.databinding.FragmentPlaceBinding
import com.example.sunny.model.CityResult
import com.example.sunny.service.ApiService
import com.example.sunny.model.CitySearchResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var placeAdapter: PlaceAdapter
    private val cityList = mutableListOf<CityResult>()
    private var toast: Toast? = null

    private var citySelectListener: ((CityResult) -> Unit)? = null
    private var searchJob: Job? = null
    
    companion object {
        private const val TAG = "PlaceFragment"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBarHeight()
        setupRecyclerView()
        setupSearch()
    }

    private fun setupStatusBarHeight() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.statusBarPlaceholder) { _, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val layoutParams = binding.statusBarPlaceholder.layoutParams
            layoutParams.height = statusBarInsets.top
            binding.statusBarPlaceholder.layoutParams = layoutParams
            insets
        }
    }

    private fun setupRecyclerView() {
        placeAdapter = PlaceAdapter(cityList)
        placeAdapter.setOnItemClickListener { city ->
            citySelectListener?.invoke(city)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = placeAdapter
        }
    }

    private fun setupSearch() {
        binding.searchPlaceEdit.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchPlaceEdit.text.toString().trim()
            if (query.isNotEmpty()) {
                searchCities(query)
            }
            true
        }

        // 添加文本变化监听器，实现实时搜索功能
        binding.searchPlaceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                Log.d(TAG, "文本改变，查询内容: '$query'")
                searchCities(query)
            }
        })
    }

    private fun searchCities(query: String) {
        if (query.isEmpty()) {
            // 如果查询为空，清空列表
            placeAdapter.updateData(emptyList())
            binding.recyclerView.visibility = View.GONE
            return
        }
        
        // 取消之前的搜索任务
        searchJob?.cancel()
        
        // 启动新的搜索任务
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            val result = ApiService.searchCity(query)

            result.onSuccess {
                updateCityList(it)
            }.onFailure {
                showError(it.message ?: "搜索失败")
            }
        }
    }

    private fun updateCityList(response: CitySearchResponse) {
        response.results?.let { cities ->
            placeAdapter.updateData(cities)
        }
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
            show()
        }
    }

    // 添加设置城市选择监听器的方法
    fun setOnCitySelectListener(listener: (CityResult) -> Unit) {
        citySelectListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toast?.cancel()
        searchJob?.cancel()
    }
}