package com.example.swensonhe.di

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.util.Util
import com.example.swensonhe.databinding.LoaderLayoutBinding
import com.example.swensonhe.util.CommonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,ActivityComponent::class,ServiceComponent::class)
class CommonDi {
   /* @Provides
    fun getApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }*/
    @Provides
    fun getloaderDialog(@ActivityContext context: Context?) : Dialog {
        val dialog = Dialog(context!!)
        val binding : LoaderLayoutBinding = LoaderLayoutBinding.inflate((context as Activity).layoutInflater)
       // val view = (context as Activity).layoutInflater.inflate(R.layout.loader_layout, null)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        binding.loaderContainer.visibility = View.VISIBLE
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }
    @Provides
    fun getUtil(): CommonUtil {
        return  CommonUtil()
    }

    @Provides
    fun getDispposible(): CompositeDisposable = CompositeDisposable()
    

}