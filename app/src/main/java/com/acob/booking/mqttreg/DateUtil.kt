package com.acob.booking.mqttreg

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wugang00 on 16/12/2017.
 */
object DateHelper {
    const val DF_SIMPLE_STRING = "dd.MM.yyyy HH:mm:ss"
    @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DF_SIMPLE_STRING, Locale.ENGLISH)
        }
    }
}

fun dateNow(): String = Date().asString()

fun timestamp(): Long = System.currentTimeMillis()

fun dateParse(s: String): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(s, ParsePosition(0))

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String): String = asString(SimpleDateFormat(format, Locale.ENGLISH))

fun Date.asString(): String = DateHelper.DF_SIMPLE_FORMAT.get().format(this)

fun Long.asDateString(): String = Date(this).asString()

fun Date.asTimeString() :String = asString("HH:mm:ss")
fun Date.asTimeHMString() :String = asString("HH:mm")

fun Date.asDateString() :String = asString("dd.MM.yyyy")