package com.acob.booking.mqttreg.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by wugang00 on 16/01/2018.
 */
@Entity(tableName = "tbl_obvote")
data class OBVote (
        @ColumnInfo(name = "voter_name") var voterName: String,
        @ColumnInfo(name = "block_id") var blockId: String,
        @ColumnInfo(name = "vote_hash") var voteHash: String,
        @ColumnInfo(name = "vote_status") var voteStatus: String,
        @ColumnInfo(name = "vote_time") var voteTime: Date

) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

}