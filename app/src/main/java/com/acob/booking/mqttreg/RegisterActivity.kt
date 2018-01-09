package com.acob.booking.mqttreg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.TextView
import com.acob.booking.mqttreg.timing.TimeInfo
import com.acob.booking.mqttreg.timing.TimeProcessor
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {
    val TAG = "REG Acitvity"
    val COUNTDOWN_TIMER_INTERVAL = 1000L
    lateinit @Inject var timeProcessor : TimeProcessor

    lateinit var cdRegister :CDTRegister
    lateinit var cdDecide :CDTDecide

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //initial time information
        newCycle()
    }

    override fun onStop() {
        cdRegister.cancel()
        super.onStop()
    }

    fun newCycle (){
        var timeInfo = TimeInfo(Calendar.getInstance().time)
        var now = Calendar.getInstance().time
        var cdRegTime = timeInfo.voteStartTime.time - now.time
        //var cdDecideTime = timeInfo.pubStartTime.time - date.time

        val nextRegSting = timeInfo.voteStartTime.asTimeString()
        val nextDecideString = timeInfo.pubStartTime.asTimeString()


        reg_tv_current_cycle.setText("${timeInfo.getCurrentCycle()} ${timeInfo.getCurrentRegisterScope()}", TextView.BufferType.NORMAL)
        reg_tv_next_reg.setText("Hash Pub on: $nextRegSting  ", TextView.BufferType.NORMAL)
        reg_tv_next_decide.setText("Block Winer on: $nextDecideString  ", TextView.BufferType.NORMAL)

        //reg_tv_next_decide_cd.setText("", TextView.BufferType.NORMAL)

        cdRegister = CDTRegister(cdRegTime,COUNTDOWN_TIMER_INTERVAL,timeInfo)
        cdRegister.start()


    }
    fun startCDDecide(timeInfoNow: TimeInfo) {
        var now = Calendar.getInstance().time
        var cdDecideTime = timeInfoNow.pubStartTime.time - now.time
        cdDecide = CDTDecide(cdDecideTime,COUNTDOWN_TIMER_INTERVAL,timeInfoNow)
        cdDecide.start()
    }

    inner class CDTRegister (cdTarget:Long,cdInterval:Long,timeInfoNow:TimeInfo):  CountDownTimer(cdTarget,cdInterval) {
        var timeInfoNow = timeInfoNow
        var currentCycle = timeInfoNow.getCurrentCycle()
        override fun onTick(millisUntilFinished: Long) {
            var cdTime = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
            reg_tv_next_reg_cd.setText("$currentCycle: $cdTime")
        }

        override fun onFinish() {
            reg_tv_next_reg_cd.setText("Register Closed")
            newCycle()
            startCDDecide(timeInfoNow)
        }
    }
    inner class CDTDecide (cdTarget:Long,cdInterval:Long,timeInfoNow:TimeInfo):  CountDownTimer(cdTarget,cdInterval) {
        var timeInfoNow = timeInfoNow
        var currentCycle = timeInfoNow.getCurrentCycle()
        override fun onTick(millisUntilFinished: Long) {
            var cdTime = DateUtils.formatElapsedTime( millisUntilFinished / 1000)
            reg_tv_next_decide_cd.setText("$currentCycle: $cdTime" )
        }

        override fun onFinish() {
            reg_tv_next_decide_cd.setText("Waiting Register close")
           // newCycle()
        }
    }
}
