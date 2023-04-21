package com.example.swensonhe.di

import android.content.Context
import com.example.swensonhe.data.remote.EndPoints
import com.example.swensonhe.data.repository.WeatherRepoImplementer
import com.example.swensonhe.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,ActivityComponent::class,ServiceComponent::class)
class WeatherRepoModule {
    @Provides
    fun getCategoryRepository(endPoints: EndPoints, @ApplicationContext context: Context?) : WeatherRepository {
      return WeatherRepoImplementer(endPoints,context!!)
    }

}