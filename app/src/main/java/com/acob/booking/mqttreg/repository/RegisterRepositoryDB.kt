package com.acob.booking.mqttreg.repository

import android.arch.lifecycle.LiveData
import android.os.HandlerThread
import android.util.Log
import com.acob.booking.mqttreg.data.dao.OBRegisterDao
import com.acob.booking.mqttreg.data.dao.OBVoteDao
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.model.OBVote
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by wugang00 on 12/01/2018.
 */
class RegisterRepositoryDB @Inject constructor(registerDao:OBRegisterDao,voteDao:OBVoteDao):IRegisterRepository {
    override fun publishVoteInfo(voteInfo: OBVote) {
        if (voteInfo !=null){
            voteDao.insert(voteInfo)
            Log.i(TAG, " vote inserted to DB")
        }
    }

    override fun getRegisterByCycle(cycle: String): Single<List<OBRegister>> {
        return registerDao.findRegisterByCycle(cycle)
    }


    val TAG = "regi repo"
    var registerDao = registerDao
    var voteDao = voteDao
    var ht = HandlerThread("")
    override fun publishRegisterInfo(liveRegister: LiveData<OBRegister>) {
        var register = liveRegister.value
        if (register !=null) {
            if (registerDao.findRegisterByEventUser(register.evtId,register.userName)==null){
                registerDao.insert(register)
                Log.i(TAG, " register inserted to DB")
            } else {
                Log.i(TAG,"user ${register?.userName} has already register event ${register?.evtId}" )
            }

        } else {
            Log.i(TAG,"cannot get reigister informaiton" )
        }

    }

    override fun getLastRegisterInfo(): Flowable<OBRegister> {
        return registerDao.getLastRegister()
    }
}