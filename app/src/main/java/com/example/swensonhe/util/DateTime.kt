package com.example.swensonhe.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

interface DateTime {
    fun getOldVersionFormatter () : SimpleDateFormat
    fun getNewVersionFormatter () : DateTimeFormatter
}

class DateImplementer (val pattern:String) : DateTime {
    override fun getOldVersionFormatter(): SimpleDateFormat {
    return  SimpleDateFormat(pattern, Locale.ENGLISH)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNewVersionFormatter(): DateTimeFormatter {
      return  DateTimeFormatter.ofPattern(pattern)
    }

}
class TimeImplementer(val pattern:String) : DateTime {
    override fun getOldVersionFormatter(): SimpleDateFormat {
        return  SimpleDateFormat(pattern, Locale.ENGLISH)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNewVersionFormatter(): DateTimeFormatter {
        return  DateTimeFormatter.ofPattern(pattern)
    }


}