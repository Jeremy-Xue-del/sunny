package com.example.sunny.view.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunny.databinding.FragmentPlaceBinding
import com.example.sunny.model.CityResult
import com.example.sunny.service.ApiService
import com.example.sunny.model.CitySearchResponse

class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var placeAdapter: PlaceAdapter
    private val cityList = mutableListOf<CityResult>()
    private var toast: Toast? = null

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
    }

    private fun searchCities(query: String) {
        ApiService.searchCity(query) { result ->
            activity?.runOnUiThread {
                result.onSuccess { response ->
                    updateCityList(response)
                }.onFailure { error ->
                    showError(error.message ?: "搜索失败")
                }
            }
        }
    }

    private fun updateCityList(response: CitySearchResponse) {
        val oldList = cityList.toList()
        cityList.clear()
        response.results?.let { cities ->
            cityList.addAll(cities)
        }
        placeAdapter.updateDataWithDiff(oldList, cityList)
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toast?.cancel()
    }
}
