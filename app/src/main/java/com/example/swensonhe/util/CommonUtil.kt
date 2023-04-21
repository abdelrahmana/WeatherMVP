package com.example.swensonhe.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swensonhe.data.model.GridModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CommonUtil {
    @Suppress("DEPRECATION")
     fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            val capabilities = connectivityManager.getNetworkCapabilities(networkCapabilities)
            capabilities?.let {
                result = it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&  capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        //     if (result)
        //        return internetIsConnected()

        return result
    }
    fun getFormattedDateTime(timeStamp : Long,zoneId: String,type : DateTime): String? {
        var answer: String = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = Instant.ofEpochSecond(timeStamp)
            val zone = ZoneId.of(zoneId)
            val formatter = /*DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")*/type.getNewVersionFormatter()
            answer = current.atZone(zone).format(formatter)
        } else {
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getTimeZone(zoneId)
            calendar.timeInMillis = ((timeStamp.toDouble())).toLong()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val date = calendar.time
            val formatter =type.getOldVersionFormatter()//SimpleDateFormat("EEEE, dd MMM yyyy", Locale.ENGLISH)
            answer = formatter.format(date)
        }
        return answer

    }
    fun setRecycleView(recyclerView: RecyclerView?, adaptor: RecyclerView.Adapter<*>,
                       verticalOrHorizontal: Int?, context: Context, gridModel: GridModel?, includeEdge : Boolean) {
        var layoutManger : RecyclerView.LayoutManager? = null
        if (gridModel==null) // normal linear
            layoutManger = LinearLayoutManager(context, verticalOrHorizontal!!,false)
        else
        {
            layoutManger = GridLayoutManager(context, gridModel.numberOfItems)
            if (recyclerView?.itemDecorationCount==0)
                recyclerView.addItemDecoration(SpacesItemDecoration(gridModel.numberOfItems, gridModel.space, includeEdge))
        }
        recyclerView?.apply {
            setLayoutManager(layoutManger)
            setHasFixedSize(true)
            adapter = adaptor

        }
    }
    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String, bundle: Bundle?, id : Int ) {

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.replace(id, fragment, tag)
        transaction.addToBackStack(tag)
        //    transaction.addToBackStack(null)
        transaction.commit()

    }
}
