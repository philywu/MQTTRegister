package com.acob.booking.mqttreg.data.dao

import android.arch.persistence.room.*
import android.os.Build.ID
import com.acob.booking.mqttreg.data.model.OBRegister
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by wugang00 on 26/12/2017.
 */
@Dao
interface OBRegisterDao: OBDao<OBRegister>{

    @Query("select * from tbl_obregister")
    fun getAllRegister(): Flowable<List<OBRegister>>

    @Query("select * from tbl_obregister where block_id = :cycle")
    fun findRegisterByCycle(cycle:String): Single<List<OBRegister>>

    @Query("select * from tbl_obregister where id = :id")
    fun findRegisterById(id: Long): OBRegister

    @Query("select * from tbl_obregister where event_id = :evtId and user_name= :userName")
    fun findRegisterByEventUser(evtId: String, userName:String): OBRegister

    @Query("SELECT * FROM tbl_obregister WHERE id = (SELECT MAX(ID)  FROM tbl_obregister)")
    fun getLastRegister(): Flowable<OBRegister>

}