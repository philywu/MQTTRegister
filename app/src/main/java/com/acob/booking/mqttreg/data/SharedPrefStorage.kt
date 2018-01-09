package com.acob.booking.mqttreg.data

import android.content.Context
import android.preference.PreferenceManager


/**
 * Created by wugang00 on 13/12/2017.
 */
class SharedPrefStorage : LocalStorage {

    private val context: Context

    constructor(context: Context) {
        this.context = context
    }
    override fun writeMessage(key:String, value: String) {
        /*
        context.getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
                .edit().putString(key, value).apply();
                */
        var editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(key,value)
        editor.commit()

    }

    override fun readMessage(key:String): String {

       return PreferenceManager.getDefaultSharedPreferences(context).getString(key,"")
        //return context.getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
        //        .getString("key", "");
    }


}