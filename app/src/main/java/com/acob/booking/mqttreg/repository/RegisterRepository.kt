package com.acob.booking.mqttreg.repository

import android.arch.lifecycle.LiveData
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.model.OBVote
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by wugang00 on 12/01/2018.
 */
class RegisterRepository:IRegisterRepository {
    override fun publishVoteInfo(voteInfo: OBVote) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRegisterByCycle(cycle: String): Single<List<OBRegister>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastRegisterInfo(): Flowable<OBRegister> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publishRegisterInfo(liveRegister: LiveData<OBRegister>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}