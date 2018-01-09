package com.acob.booking.mqttreg.timing

import org.junit.Test
import java.util.*

/**
 * Created by wugang00 on 6/01/2018.
 */
class TimeInfoTest {
    @Test
    fun testCreate() {
        var timeInfo = TimeInfo(Calendar.getInstance())
        var d = timeInfo.voteStartTime
        println(d)
    }
    @Test
    fun testGetCurrentCycle() {
        var c = Calendar.getInstance()

        var timeInfo = TimeInfo(c)
       // var c1 = Calendar.getInstance()
        println(timeInfo.getCurrentCycle())
       // println("${c1.time.time} vs ${timeInfo.date.time}")

    }
}