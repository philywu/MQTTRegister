package com.acob.booking.mqttreg.repository


import android.arch.lifecycle.LiveData
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.model.OBVote
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by wugang00 on 12/01/2018.
 */
interface IRegisterRepository{
    fun publishRegisterInfo(livRegister: LiveData<OBRegister>)
    fun getLastRegisterInfo(): Flowable<OBRegister>
    fun getRegisterByCycle(cycle:String): Single<List<OBRegister>>
    fun publishVoteInfo(voteInfo: OBVote)
}