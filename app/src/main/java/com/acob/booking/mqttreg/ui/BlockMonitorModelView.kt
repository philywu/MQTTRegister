package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import com.acob.booking.mqttreg.timing.TimeInfo
import android.arch.lifecycle.LiveData
import android.content.ContentValues.TAG
import android.util.Log
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.R.id.spinner_event
import java.util.*
import java.util.stream.Collectors.toList
import kotlin.collections.ArrayList


/**
 * Created by wugang00 on 7/01/2018.
 */
class BlockMonitorModelView: ViewModel() {

    private var list = ArrayList<TimeInfo>()
    private var timeInfoList: MutableLiveData<ArrayList<TimeInfo>> = MutableLiveData<ArrayList<TimeInfo>>()
    private var selectedEvent: MutableLiveData<String> = MutableLiveData<String>()
    private var selectedUser: MutableLiveData<String> = MutableLiveData<String>()
    var eventList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var userList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()

    fun initialData() {
        eventList.value = arrayListOf(
                "Party",
                "Football",
                "BBQ",
                "Learning"
        )
        userList.value = arrayListOf(
                "Phily",
                "Mason",
                "Luke",
                "Will",
                "Kevin",
                "Danny",
                "Tony"

        )


    }

    fun getList(): LiveData<List<TimeInfo>>{
        return timeInfoList as LiveData<List<TimeInfo>>
    }
    fun insertTimeInfo(timeInfo:TimeInfo):BlockMonitorModelView {
        list.add(timeInfo)
        //delete if list size > 3
        if (list.size>3){
            list.removeAt(0)
        }
        //timeInfoList.value = list
        return this
    }
    fun updateTimeInfo(timeInfo:TimeInfo):BlockMonitorModelView{
        var found = false
        for (i in list.indices) {
            var t = list[i]
            if (t.getCurrentCycle() == timeInfo.getCurrentCycle()){
                list.set(i,timeInfo)
                found = true
                break
            }
        }
        return this

    }
    fun updateLiveData():BlockMonitorModelView{
        timeInfoList.value  = list
        return this
    }
    fun getSelectedEvent():LiveData<String>{
        return selectedEvent
    }
    fun setSelectedEvent(event:String) {
        selectedEvent.value = event
    }

    fun initialDisplay() {
        if (list.isEmpty()){
            var now = Calendar.getInstance().time
            //get current Time Info
            var currentTimeInfo = TimeInfo(now)
            //get previous TimeInfo
            var lastTimeInfo = TimeInfo(Date(currentTimeInfo.date.time-currentTimeInfo.registerInterval))
            lastTimeInfo.setStatus(now)

            //set current TimeInfo
            insertTimeInfo(lastTimeInfo)
                    .insertTimeInfo(currentTimeInfo)
                    .updateLiveData()
        }
    }

    fun endAction(tvid:Int,timeInfo:TimeInfo){

        when (tvid){
            R.id.tc_reg -> {
                var timeInfoNew = TimeInfo(Calendar.getInstance().time)

                timeInfo.currentStatus= timeInfo.STATUS_REGISTER_END
                insertTimeInfo(timeInfoNew)
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
        updateTimeInfo(timeInfo).updateLiveData()
    }

    fun setSelectedValue(spinnerId: Int, selectedItem: String) {
        when (spinnerId) {
            R.id.spinner_event -> {
                selectedEvent.value = selectedItem
            }
            R.id.spinner_event -> {
                selectedUser.value = selectedItem
            }
        }
    }
    fun setSelection(sel:String?,list:ArrayList<String>?):Int{

        if (!sel.isNullOrBlank() && list!!.isNotEmpty()){
            return list.indexOf(sel)

        } else {
            return -1
        }
    }
    fun setSelectionEvent(): Int {
        return setSelection(selectedEvent.value, eventList.value)

    }
    fun setSelectionUser(): Int {
        return setSelection(selectedUser.value, userList.value)

    }

}