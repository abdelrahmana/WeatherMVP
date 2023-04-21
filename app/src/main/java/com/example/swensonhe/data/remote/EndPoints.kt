package com.example.swensonhe.data.remote

import com.example.swensonhe.util.Constant.GETWEATHER
import com.example.swensonhe.data.model.WeatherResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EndPoints {
    @GET(GETWEATHER)
    suspend fun getCurrentWeather(@QueryMap hashMap: HashMap<String,String>
    ): ApiResponse<WeatherResponse>

}