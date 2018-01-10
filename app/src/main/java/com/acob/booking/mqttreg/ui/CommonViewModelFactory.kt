package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.acob.booking.mqttreg.data.AppDB
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.message.MqttManager
import com.google.gson.Gson
import javax.inject.Inject
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.acob.booking.mqttreg.message.MessageProcesserDB
import com.acob.booking.mqttreg.rx.SchedulersFacade


/**
 * Created by wugang00 on 10/01/2018.
 */
class CommonViewModelFactory @Inject constructor(messageProcesser:MessageProcesserDB, gson: Gson, localStorage: LocalStorage,schedulersFacade: SchedulersFacade): ViewModelProvider.Factory {

    var messageProcesser = messageProcesser
    var gson = gson
    var localStorage = localStorage
    var schedulersFacade = schedulersFacade
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockMonitorModelView::class.java)) {
            return BlockMonitorModelView(messageProcesser, gson, localStorage,schedulersFacade) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}