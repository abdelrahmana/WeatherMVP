package com.example.swensonhe.di

import android.content.Context
import com.example.swensonhe.BuildConfig
import com.example.swensonhe.util.Constant.ACCEPTKEY
import com.example.swensonhe.util.Constant.ACCEPTVALUE
import com.example.swensonhe.util.Constant.TOKENENDPOINT
import com.example.swensonhe.util.Constant.TOKENKEY
import com.example.swensonhe.util.Constant.WEATHERKEY
import com.example.swensonhe.data.remote.EndPoints
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(@ApplicationContext applicationContext: Context): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getAuthInterceptor(TOKENENDPOINT,applicationContext))
            .addInterceptor(logging)
            .build()
    }
    @Provides
    fun getAuthInterceptor(token : String,applicationContext: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val url: HttpUrl =
                original.url.newBuilder().addQueryParameter(WEATHERKEY,
                    BuildConfig.weatherKey).build() // add query in request
            chain.proceed(
                original.newBuilder().url(url)
                    .header(ACCEPTKEY, ACCEPTVALUE)
                    .header(TOKENKEY, token)
                    //.header(WEATHERKEY, BuildConfig.weatherKey)
                    .method(original.method, original.body)
                    .build()
            )
        }
    }

    @Singleton
    @Provides
    fun provideEndPoints(client: OkHttpClient): EndPoints {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .build()
            .create(EndPoints::class.java)
    }

}