package com.acob.booking.mqttreg.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.acob.booking.mqttreg.MyApplication
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.message.MessageProcesserDB
import com.acob.booking.mqttreg.repository.IRegisterRepository
import com.acob.booking.mqttreg.rx.SchedulersFacade
import com.google.gson.Gson
import javax.inject.Inject


/**
 * Created by wugang00 on 10/01/2018.
 */
class RegisterViewModelFactory @Inject constructor(gson: Gson, localStorage: LocalStorage, registerRepository: IRegisterRepository): ViewModelProvider.Factory {


    var gson = gson
    var localStorage = localStorage
    var registerRepository = registerRepository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockMonitorModelView::class.java)) {
            return BlockMonitorModelView(gson, localStorage,registerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}