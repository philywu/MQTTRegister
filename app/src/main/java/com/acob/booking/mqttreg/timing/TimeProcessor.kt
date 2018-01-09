package com.acob.booking.mqttreg.timing

import android.content.Context
import com.acob.booking.mqttreg.data.LocalStorage

import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by wugang00 on 5/01/2018.
 */
class TimeProcessor  @Inject constructor(appContext: Context, gson: Gson, lStorage: LocalStorage) {
        val TAG = "Time Processor"
        var gson = gson
        var lStorage = lStorage
        var appContext = appContext

  /*  fun getNextRegisterTime():Date{
       // var d = TimeUtil.getNextRoundTime
        var date = TimeUtil.getCurrentDateTime()
        var today = TimeUtil.getTodayStartDate(Calendar.getInstance())

        //var d = TimeUtil.getNextRoundTime(today,interval,cycle,delay)

        Log.d(TAG,"next Round Time: " +d )
        return d
    }
    fun getPubStartTime(startTime:Date):Date {
        //var d =



        return d
    }*/


}