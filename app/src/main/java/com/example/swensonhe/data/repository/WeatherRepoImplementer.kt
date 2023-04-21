package com.example.swensonhe.data.repository

import android.content.Context
import com.example.swensonhe.R
import com.example.swensonhe.data.model.WeatherResponse
import com.example.swensonhe.data.remote.EndPoints
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherRepoImplementer @Inject constructor(val endPoints: EndPoints, @ApplicationContext val context : Context) : WeatherRepository{
    override suspend fun getCurrentForecast(
        queryMap: HashMap<String, String>,
        completion: (WeatherResponse?, String?) -> Unit
    ) {
        val response =  endPoints.getCurrentWeather(queryMap)
        response.onSuccess {
            completion.invoke(data,null)
        }
        response.onException {
            completion.invoke(null,context.getString(R.string.error_happend))
        }
        response.onError {
            completion.invoke(null,context.getString(R.string.error_happend))

        }
    }
}