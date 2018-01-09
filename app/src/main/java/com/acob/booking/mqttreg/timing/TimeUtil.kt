package com.acob.booking.mqttreg.timing

import android.text.format.DateUtils
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by wugang00 on 5/01/2018.
 */
object TimeUtil {

    fun getTodayStartDate(cal:Calendar): Date {

        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        return cal.time
    }
    fun getCurrentDateTime() :Date {
        val today = Calendar.getInstance()
        return today.time
    }
    fun getNextCycle(startTime:Date,endTime:Date,intervalMs:Long):Long {
        var diff = endTime.time - startTime.time
        var nextCycle = diff/intervalMs + 1
        return nextCycle
    }
    fun getNextRoundTime(startTime:Date,interval:Long,cycle:Long,delayMs:Long):Date{
        return Date( startTime.time + interval * cycle + delayMs)
    }
    fun getMs(interval:Long ,t:TimeUnit):Long{
        return t.toMillis(interval)
    }
    fun convertToHMS (seconds:Long):IntArray{

        val hours = seconds.toInt() / 3600
        var remainder = seconds.toInt() - hours * 3600
        val mins = remainder / 60
        remainder = remainder - mins * 60
        val secs = remainder

        return intArrayOf(hours, mins, secs)
    }
}