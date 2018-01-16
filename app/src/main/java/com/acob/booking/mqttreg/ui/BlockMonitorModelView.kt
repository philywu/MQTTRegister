package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentValues.TAG
import android.util.Log
import com.acob.booking.mqttreg.MSG_TYPE_REGISTER
import com.acob.booking.mqttreg.MSG_TYPE_TIMER
import com.acob.booking.mqttreg.MSG_TYPE_VOTE
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.data.OBMessage
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.model.OBVote
import com.acob.booking.mqttreg.data.util.MerkleTrees
import com.acob.booking.mqttreg.repository.IRegisterRepository
import com.acob.booking.mqttreg.rx.SchedulersFacade
import com.acob.booking.mqttreg.timing.TimeInfo
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * Created by wugang00 on 7/01/2018.
 */
class BlockMonitorModelView @Inject constructor( gson: Gson,localStorage: LocalStorage,repository:IRegisterRepository ) :ViewModel() {


    var gson = gson
    var localStorage = localStorage
    var repository = repository


    private val disposables = CompositeDisposable()

    private var list = ArrayList<TimeInfo>()
    private var timeInfoList: MutableLiveData<ArrayList<TimeInfo>> = MutableLiveData<ArrayList<TimeInfo>>()
    private var selectedEvent: MutableLiveData<String> = MutableLiveData<String>()
    private var selectedUser: MutableLiveData<String> = MutableLiveData<String>()
    private var currentRegister:MutableLiveData<OBRegister> = MutableLiveData<OBRegister>()
    var eventList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var userList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()

    var monitorList: MutableLiveData<ArrayList<OBMessage>> = MutableLiveData<ArrayList<OBMessage>>()

    override fun onCleared() {
        disposables.clear()
    }
    fun initialData(startDate:Date) {
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
        monitorList.value = arrayListOf(
            OBMessage("Start Monitor", MSG_TYPE_TIMER,startDate)
        )


    }

    fun showRegister(startTime:Date){
        var now = Date()
        disposables.add(repository.getLastRegisterInfo()
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
                .subscribe(
                        {
                            if (it!=null) {
                                if (it.createTime.time >= startTime.time) {
                                    var obmsg = OBMessage("${it.userName} registered ${it.evtId} on ${it.blockId}", MSG_TYPE_REGISTER, it.createTime)
                                    addToMonitorList(obmsg)
                                }
                            }
                        }
                )

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
                addToMonitorList(OBMessage("Register End for Cycle ${timeInfo.getCurrentCycle()}",MSG_TYPE_TIMER,timeInfo.registerEndTime))

            }
            R.id.tc_vote -> {
                timeInfo.currentStatus= timeInfo.STATUS_VOTING_START
                Log.d(TAG,"TC Vote Start" )

                addToMonitorList(OBMessage("Vote Start for Cycle ${timeInfo.getCurrentCycle()}",MSG_TYPE_TIMER,timeInfo.voteStartTime))
                voteForCycle(timeInfo.getCurrentCycle())
            }
            R.id.tc_pub -> {
                timeInfo.currentStatus= timeInfo.STATUS_PUB_BLOCK_START
                Log.d(TAG,"TC Pub Start" )
                addToMonitorList(OBMessage("Publish start for Cycle ${timeInfo.getCurrentCycle()}",MSG_TYPE_TIMER,timeInfo.pubStartTime))
            }

        }
        updateTimeInfo(timeInfo).updateLiveData()


    }

    private fun voteForCycle(cycle: String) {
        disposables.add(repository.getRegisterByCycle(cycle)
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())

                .subscribe(
                        {
                            var strList = arrayListOf<String>()
                            if (it!=null && it.isNotEmpty()) {
                                it.forEach{
                                    obreg ->
                                    run {
                                        strList.add(obreg.toString())
                                        Log.d(TAG,"register to vote: ${obreg.toString()} ")
                                    }
                                }
                                val sortedList = strList.sortedWith(compareBy{it})
                                //generate merkel tree
                                var root = getMerkelTreeRoot(sortedList)

                                //publish vote information
                                publishVote("Phily",cycle,root)
                                publishVote("Luke",cycle,root)
                                publishVote("Mason",cycle,root)
                                publishVote("Kevin",cycle,root)
                                publishVote("Wrong1",cycle,root+"1")
                                publishVote("Wrong2",cycle,root+"2")
                                var obmsg = OBMessage("send hash :$root ", MSG_TYPE_VOTE, Date())
                                addToMonitorList(obmsg)
                            }

                        },
                        { e-> run {
                            e.printStackTrace()
                        }// do nothing

                            }

                )

        )

    }

    private fun publishVote(voteName:String,cycle: String, root: String) {

        if (root !="") {
            var vote = OBVote(voteName, cycle, root, "Ready", Date())
            disposables.add(

                    Observable.fromCallable(
                            {
                                repository.publishVoteInfo(vote)
                                vote
                            })

                            .subscribeOn(SchedulersFacade.io())
                            .observeOn(SchedulersFacade.ui())
                            .subscribe(
                                    { obj ->
                                        run {
                                            Log.d(TAG,"publish ${obj.blockId} successfully"

                                            )
                                        } },
                                    { throwable -> run {throwable.printStackTrace()} }
                            )
            )


        }
    }

    private fun getMerkelTreeRoot(list: List<String>): String {
        if (list.size>0) {
            var mt = MerkleTrees(list)
            mt.merkle_tree()
            return mt.root
        } else {
            return ""
        }
    }

    fun addToMonitorList(obmsg:OBMessage){
        var l = monitorList.value
        l?.add(obmsg)
        monitorList.value = l
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
        currentRegister.value = o

        success = disposables.add(

                        Observable.fromCallable(
                                {
                                    repository.publishRegisterInfo(currentRegister)

                                    o
                                })

                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
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