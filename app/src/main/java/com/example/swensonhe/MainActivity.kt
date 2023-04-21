package com.example.swensonhe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.swensonhe.ui.weatherfragment.WeatherFragment
import com.example.swensonhe.util.CommonUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var commonUtil: CommonUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        commonUtil.changeFragmentBack(
            this,
            WeatherFragment(),
            "",
            null,
            R.id.frame_container
        )
    }
}