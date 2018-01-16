package com.acob.booking.mqttreg

import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.acob.booking.mqttreg.data.OBMessage


/**
 * Created by wugang00 on 4/01/2018.
 */
class MessageAdapter(private val activity: Activity, resource: Int, private val messages: List<OBMessage>,val currentUser:String) : ArrayAdapter<OBMessage>(activity, resource, messages) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        val inflater = activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var layoutResource = 0 // determined by view type
        val obMsg = getItem(position)
        val viewType = getItemViewType(position)
        var bubbleMessage = ""
        if (currentUser != obMsg.sender) {
            layoutResource = R.layout.monitor_list_item
            bubbleMessage = "${obMsg.sender}: "
        } else {
            layoutResource = R.layout.right_chat_bubble

        }

       // layoutResource = R.layout.right_chat_bubble
        /*if (convertView != null) {
            holder = convertView!!.getTag()
        } else {*/
            convertView = inflater.inflate(layoutResource, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.setTag(holder)
        //}

        //set message content
        holder.msg.text = bubbleMessage + obMsg!!.body
        holder.dt.text =  obMsg!!.time.asString()

        return convertView
    }

    override fun getViewTypeCount(): Int {
        // return the total number of view types. this value should never change
        // at runtime. Value 2 is returned because of left and right views.
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2
    }

    private inner class ViewHolder(v: View) {
        val msg: TextView
        var dt :TextView
        init {
            msg = v.findViewById(R.id.txt_msg)
            dt = v.findViewById(R.id.txt_time)
        }
    }
}