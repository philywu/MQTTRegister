package com.acob.booking.mqttreg.data

/**
 * Created by wugang00 on 13/12/2017.
 */
interface LocalStorage {
    fun writeMessage(key:String, value: String)
    fun readMessage(key:String): String
}