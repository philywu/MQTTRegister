package com.acob.booking.mqttreg.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.acob.booking.mqttreg.data.dao.OBRegisterDao
import com.acob.booking.mqttreg.data.model.OBRegister
import com.acob.booking.mqttreg.data.util.DateConverter


/**
 * Created by wugang00 on 11/12/2017.
 */


@Database(entities = arrayOf(

        OBRegister::class

), version = 4, exportSchema = false)

@TypeConverters( DateConverter::class)
abstract class AppDB : RoomDatabase() {

    abstract fun obRegisterDao(): OBRegisterDao

}

