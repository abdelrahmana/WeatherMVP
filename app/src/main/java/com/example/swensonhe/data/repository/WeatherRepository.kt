package com.example.swensonhe.data.repository

import com.example.swensonhe.data.model.WeatherResponse
import com.skydoves.sandwich.ApiResponse

interface WeatherRepository {
    suspend fun getCurrentForecast( queryMap: HashMap<String,String>,completion: (WeatherResponse?, String?) -> Unit)
}