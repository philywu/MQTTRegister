package com.acob.booking.mqttreg.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.model.OBVote
import io.reactivex.Single

/**
 * Created by wugang00 on 16/01/2018.
 */
@Dao
interface OBVoteDao: OBDao<OBVote> {

    @Query("select * from tbl_obvote where block_id = :cycle")
    fun findAllVoteByCycle(cycle:String): Single<List<OBVote>>


}