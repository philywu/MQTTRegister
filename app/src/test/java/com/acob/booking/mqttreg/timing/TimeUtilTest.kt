package com.acob.booking.mqttreg.timing

import org.junit.Test
import java.util.*

/**
 * Created by wugang00 on 5/01/2018.
 */

open class TimeUtilTest {

    @Test
    fun testGetTodayStartDate() {
        var today = TimeUtil.getTodayStartDate(Calendar.getInstance())
        println(today)
        assert(true)
    }
    @Test
    fun testGetCurrentDateTime() {
        var now = TimeUtil.getCurrentDateTime()
        println(now)
        assert (true)
    }
    @Test
    fun testGetNextRoundTime() {
//        var d = TimeUtil.getNextRoundTime()
//        println(d)
        assert (true)
    }
}