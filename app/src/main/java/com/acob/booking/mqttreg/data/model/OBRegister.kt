package com.acob.booking.mqttreg.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by wugang00 on 26/12/2017.
 */
@Entity(tableName = "tbl_obregister")
data class OBRegister(
        @ColumnInfo(name = "block_id") var blockId: String,
        @ColumnInfo(name = "event_id") var evtId: String,
        @ColumnInfo(name = "user_name") var userName: String,
        @ColumnInfo(name = "status") var status: String,
        @ColumnInfo(name = "create_t") var createTime: Date

) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}