package com.example.swensonhe.ui.weatherfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swensonhe.data.model.WeatherResponse
import com.example.swensonhe.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val weatherRepository: WeatherRepository): ViewModel() {
    private val _MutableForceast = MutableLiveData<WeatherResponse?>()
    var forecastLiveData : LiveData<WeatherResponse?> = _MutableForceast
    private val _error = MutableLiveData<String?>()
    var errorLiveData : LiveData<String?> = _error
    fun getForecast(hashMap: HashMap<String,String>) { // set category
        // load loader of internet here
        viewModelScope.launch {
            weatherRepository.getCurrentForecast(hashMap){forecast,error->
                forecast?.let {
                    _MutableForceast.value =  forecast// set value to implementer
                }
                error?.let {
                    _error.value = it
                }
            }
        }
    }

}