package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import com.acob.booking.mqttreg.timing.TimeInfo
import android.arch.lifecycle.LiveData
import android.content.ContentValues.TAG
import android.util.Log
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.R.id.spinner_event
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.message.MessageProcesserDB
import com.google.gson.Gson
import java.util.*
import java.util.stream.Collectors.toList
import javax.inject.Inject
import kotlin.collections.ArrayList
import android.text.method.TextKeyListener.clear
import com.acob.booking.mqttreg.rx.SchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import android.os.AsyncTask.execute
import io.reactivex.Observable


/**
 * Created by wugang00 on 7/01/2018.
 */
class BlockMonitorModelView @Inject constructor(messageProcesser: MessageProcesserDB, gson: Gson,localStorage: LocalStorage,schedulersFacade:SchedulersFacade ) :ViewModel() {

    var  messageProcessor  = messageProcesser
    var gson = gson
    var localStorage = localStorage
    var schedulersFacade = schedulersFacade

    private val disposables = CompositeDisposable()

    private var list = ArrayList<TimeInfo>()
    private var timeInfoList: MutableLiveData<ArrayList<TimeInfo>> = MutableLiveData<ArrayList<TimeInfo>>()
    private var selectedEvent: MutableLiveData<String> = MutableLiveData<String>()
    private var selectedUser: MutableLiveData<String> = MutableLiveData<String>()
    var eventList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var userList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()


    override fun onCleared() {
        disposables.clear()
    }
    fun initialData() {
        eventList.value = arrayListOf(

                "Football",
                "Party",
                "BBQ",
                "Learning"
        )
        userList.value = arrayListOf(

                "Mason",
                "Phily",
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
            R.id.spinner_user -> {
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

    fun doRegister():Boolean {
        var success = false
        var now = Calendar.getInstance().time
        var blockId  = timeInfoList.value!!.last().getCurrentCycle()
        var o = OBRegister(
                blockId,
                selectedEvent.value!!,
                selectedUser.value!!,
                "Pending",
                now

        )

        success = disposables.add(

                        Observable.fromCallable(
                                {
                                    messageProcessor.messagePublish(messageProcessor.msgTopicRegisterPrefix,o,messageProcessor.msgQos)
                                    o
                                })

                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(
                        { obj ->
                            run {
                                Log.d(TAG,"insert done ${obj.evtId}"

                                )
                            } },
                        { throwable -> run {throwable.printStackTrace()} }
                )
        )

        return success
    }


}