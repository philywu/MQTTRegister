package com.acob.booking.mqttreg.timing

import com.acob.booking.mqttreg.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by wugang00 on 6/01/2018.
 */
class TimeInfo constructor(date:Date) {
    val STATUS_REGISTER = 0
    val STATUS_REGISTER_END = 5
    val STATUS_VOTING_START = 10
    val STATUS_VOTING_END = 15
    val STATUS_PUB_BLOCK_START = 20
    val STATUS_COMPLETE = 99


    var date:Date = date
    var calendar = setCalendarByDate(date)
    var today :Date = TimeUtil.getTodayStartDate(calendar)
    var registerInterval = TimeUtil.getMs(TIME_REGISTER_INTEVAL, TimeUnit.MINUTES)
    var voteInterval = TimeUtil.getMs(TIME_VOTE_INTERVAL,TimeUnit.MINUTES)
    var registerDelay = TimeUtil.getMs(TIME_REGISTER_DELAY, TimeUnit.MINUTES)
    var voteDelay = TimeUtil.getMs(TIME_VOTE_DELAY, TimeUnit.MINUTES)
    var registerCycle = TimeUtil.getNextCycle(today, this.date,registerInterval)
    var voteStartTime = TimeUtil.getNextRoundTime(today,registerInterval,registerCycle,registerDelay)
    var voteEndTime =   TimeUtil.getNextRoundTime(voteStartTime, voteInterval,1,0)
    var pubStartTime =   TimeUtil.getNextRoundTime(voteStartTime, voteInterval,1,voteDelay)
    var registerStartTime = TimeUtil.getNextRoundTime(today,registerInterval,registerCycle-1,0)
    var registerEndTime = TimeUtil.getNextRoundTime(today,registerInterval,registerCycle,0)
    var currentStatus = setStatus(date)

    fun getCurrentCycle():String{
        var dateStr = today.asString("yyyyMMdd")
        return "${dateStr}_$registerCycle"
    }
    fun getCurrentRegisterScope():String {
       return "${registerStartTime.asTimeHMString()}-${registerEndTime.asTimeHMString()}"
    }
    fun setStatus(d:Date):Int{
        var dt = d.time
        currentStatus = when {
            dt> pubStartTime.time -> STATUS_PUB_BLOCK_START
            dt> voteStartTime.time -> STATUS_VOTING_START
            dt >registerEndTime.time -> STATUS_REGISTER_END
            else -> STATUS_REGISTER
        }
        return currentStatus
    }
    private fun setCalendarByDate(date: Date):Calendar {
        var cal = Calendar.getInstance()
        cal.time = this.date
        return cal
    }



}