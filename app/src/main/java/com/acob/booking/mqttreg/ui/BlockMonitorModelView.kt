package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import com.acob.booking.mqttreg.timing.TimeInfo
import android.arch.lifecycle.LiveData
import java.util.stream.Collectors.toList


/**
 * Created by wugang00 on 7/01/2018.
 */
class BlockMonitorModelView: ViewModel() {

    private var list = ArrayList<TimeInfo>()
    private var timeInfoList: MutableLiveData<ArrayList<TimeInfo>> = MutableLiveData<ArrayList<TimeInfo>>()


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

}