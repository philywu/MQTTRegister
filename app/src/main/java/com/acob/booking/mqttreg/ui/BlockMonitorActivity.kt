package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.timing.TimeInfo
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_regiser_info.*
import kotlinx.android.synthetic.main.table_row_reg_info.view.*
import java.util.*

class BlockMonitorActivity : AppCompatActivity() {
    val TAG = "BLOCK MONI"
    val COUNTDOWN_TIMER_INTERVAL = 1000L
    val CELL_CLOSE = "Close"
    val CELL_PENDING = "Pending"
    lateinit var viewModel:BlockMonitorModelView
    var cdReg :CDTBlock? = null
    var cdVote :CDTBlock? = null
    var cdPub :CDTBlock? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_monitor)

        viewModel = ViewModelProviders.of(this).get(BlockMonitorModelView::class.java)

        //addRows()
        //deleteRow ("1 1")


    }
    override fun onStart() {
        super.onStart()
        var ld = viewModel.getList()
        var timeList = ld.value
        // when first time the app start
        if (timeList ==null){
            var now = Calendar.getInstance().time
            //get current Time Info
           var currentTimeInfo = TimeInfo(now)
            //get previous TimeInfo
            var lastTimeInfo = TimeInfo(Date(currentTimeInfo.date.time-currentTimeInfo.registerInterval))
            lastTimeInfo.setStatus(now)

            //set current TimeInfo
            viewModel.insertTimeInfo(lastTimeInfo)
                    .insertTimeInfo(currentTimeInfo)
                    .updateLiveData()
        }

        viewModel.getList().observe(this,  Observer<List<TimeInfo>>{
            list ->
            run {
                Log.d(TAG, "Here" + list?.size)
                displayTableRows(list)

            }
        })

    }
    private fun deleteRow(blcNo: String) {
        var rowCount = details_table.childCount
        lateinit var tr : TableRow
        for (i in 1 until rowCount) {
            tr = details_table.getChildAt(i) as TableRow
            var v = tr.tc_blc_id.text.toString()
            Log.d(TAG,v)
            if (v == blcNo) {
                break
            }
        }
        if (tr!=null) {
            details_table.removeView(tr)
        }
    }

    private fun clearCDT(){
        if (cdReg !=null) {
            cdReg?.cancel()
        }
        if (cdVote !=null) {
            cdVote?.cancel()
        }
        if (cdPub !=null) {
            cdPub?.cancel()
        }

    }
    override fun onStop(){
       clearCDT()
        super.onStop()
    }
    override fun onPause(){
        clearCDT()
        super.onPause()
    }
    private fun displayTableRows(list:List<TimeInfo>?) {
        //clearning first

        clearCDT()

        var rowCount = details_table.childCount
        if (rowCount>0) {
            details_table.removeViews(1, rowCount-1)
        }

        var now = Calendar.getInstance().time

        if (list !=null) {
            for (timeInfo in list) {
             val tr = layoutInflater.inflate(R.layout.table_row_reg_info, null) as TableRow
                tr.tc_blc_id.setText("${timeInfo.getCurrentCycle()}")
                tr.tc_range.setText("${timeInfo.getCurrentRegisterScope()}")

                tr.tc_reg.setText(CELL_PENDING)
                tr.tc_vote.setText(CELL_PENDING)
                tr.tc_pub.setText(CELL_PENDING)

                when (timeInfo.currentStatus) {
                    timeInfo.STATUS_REGISTER -> {
                        var cdTime = timeInfo.registerEndTime.time - now.time
                        cdReg = CDTBlock(cdTime,COUNTDOWN_TIMER_INTERVAL,timeInfo,tr.tc_reg)
                        cdReg?.start()
                    }
                    timeInfo.STATUS_REGISTER_END -> {
                        var cdTime = timeInfo.voteStartTime.time - now.time
                        tr.tc_reg.setText(CELL_CLOSE)
                        cdVote = CDTBlock(cdTime,COUNTDOWN_TIMER_INTERVAL,timeInfo,tr.tc_vote)
                        cdVote?.start()
                    }
                    timeInfo.STATUS_VOTING_START  -> {
                        var cdTime = timeInfo.pubStartTime.time - now.time
                        tr.tc_reg.setText(CELL_CLOSE)
                        tr.tc_vote.setText(CELL_CLOSE)
                        cdPub = CDTBlock(cdTime,COUNTDOWN_TIMER_INTERVAL,timeInfo,tr.tc_pub)
                        cdPub?.start()
                    }
                    timeInfo.STATUS_PUB_BLOCK_START -> {
                        tr.tc_reg.setText(CELL_CLOSE)
                        tr.tc_vote.setText(CELL_CLOSE)
                        tr.tc_pub.setText(CELL_CLOSE)
                    }
                }


                details_table.addView(tr)
            }

        }

    }
    fun endAction(tvid:Int,timeInfo:TimeInfo){

        when (tvid){
            R.id.tc_reg -> {
                var timeInfoNew = TimeInfo(Calendar.getInstance().time)

                timeInfo.currentStatus= timeInfo.STATUS_REGISTER_END
                viewModel.insertTimeInfo(timeInfoNew)
                Log.d(TAG," TC Reg End")

            }
            R.id.tc_vote -> {
                timeInfo.currentStatus= timeInfo.STATUS_VOTING_START
                Log.d(TAG,"TC Vote Start" )

            }
            R.id.tc_pub -> {
                timeInfo.currentStatus= timeInfo.STATUS_PUB_BLOCK_START
                Log.d(TAG,"TC Pub Start" )
            }

        }
        viewModel.updateTimeInfo(timeInfo).updateLiveData()
    }

    inner class CDTBlock(cdTarget:Long,cdInterval:Long,timeInfoNow:TimeInfo,tv:TextView):  CountDownTimer(cdTarget,cdInterval) {
        var timeInfoNow = timeInfoNow
        var tv = tv
        var currentCycle = timeInfoNow.getCurrentCycle()
        override fun onTick(millisUntilFinished: Long) {
            var cdTime = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
            tv.setText("$cdTime")
        }

        override fun onFinish() {
            endAction(tv.id,timeInfoNow)
//            newCycle()
//            startCDDecide(timeInfoNow)
        }
    }
}
