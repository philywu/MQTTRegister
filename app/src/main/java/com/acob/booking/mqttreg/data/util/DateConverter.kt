package com.acob.booking.mqttreg.data.util

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by wugang00 on 14/12/2017.
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) {
            null
        } else {
            date.getTime()
        }
    }
}
