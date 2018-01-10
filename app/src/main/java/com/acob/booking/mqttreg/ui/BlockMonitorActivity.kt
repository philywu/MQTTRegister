package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues.TAG
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.timing.TimeInfo
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_regiser_info.*
import kotlinx.android.synthetic.main.table_row_reg_info.view.*
import java.util.*
import com.acob.booking.mqttreg.R.id.details_table
import kotlinx.android.synthetic.main.activity_block_monitor.*


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
        viewModel.initialData()

        var itemSelectLisenter = ItemSelectedListner()

        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, viewModel.eventList.value)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner_event.adapter = dataAdapter
        spinner_event.onItemSelectedListener = itemSelectLisenter
        spinner_event.setSelection(viewModel.setSelectionEvent())

        val dataAdapterUser = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, viewModel.userList.value)
        // Drop down layout style - list view with radio button
        dataAdapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner_user.adapter = dataAdapterUser
        spinner_user.onItemSelectedListener = itemSelectLisenter
        spinner_user.setSelection(viewModel.setSelectionUser())


     /*   viewModel.getSelectedEvent().observe(this,Observer<String>{
            item ->
            run {
                Log.d(TAG, "Selected Item: " + item)
                var idx = viewModel.eventList.value?.indexOf(item)
                if (idx!! >=0) {
                        spinner_event.setSelection(idx)
                }
            }
        })*/


    }



    override fun onStart() {
        super.onStart()

//set up timer header table
        viewModel.initialDisplay()
        viewModel.getList().observe(this,  Observer<List<TimeInfo>>{
            list ->
            run {
                Log.d(TAG, "Here" + list?.size)
                displayTableRows(list)

            }
        })

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


    inner class CDTBlock(cdTarget:Long,cdInterval:Long,timeInfoNow:TimeInfo,tv:TextView):  CountDownTimer(cdTarget,cdInterval) {
        var timeInfoNow = timeInfoNow
        var tv = tv

        override fun onTick(millisUntilFinished: Long) {
            var cdTime = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
            tv.setText("$cdTime")
        }

        override fun onFinish() {
            viewModel.endAction(tv.id,timeInfoNow)
//            newCycle()
//            startCDDecide(timeInfoNow)
        }
    }
    inner class ItemSelectedListner : AdapterView.OnItemSelectedListener{

        override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int,
                                    id: Long) {
            if (position >=0) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG,"ID:${parent.id} Selected: $selectedItem" )
                viewModel.setSelectedValue(parent.id, selectedItem)
            }
        }
        override fun onNothingSelected(arg0: AdapterView<*>) {

        }
    }

}
