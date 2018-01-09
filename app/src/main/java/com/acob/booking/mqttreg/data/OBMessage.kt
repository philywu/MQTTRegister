package com.acob.booking.mqttreg.data

import java.util.*

/**
 * Created by wugang00 on 4/01/2018.
 */
data class OBMessage (
        var body:String,
        var sender:String,
        var time:Date
){}