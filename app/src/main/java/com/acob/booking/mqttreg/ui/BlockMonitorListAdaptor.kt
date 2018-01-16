package com.acob.booking.mqttreg.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acob.booking.mqttreg.MSG_TYPE_REGISTER
import com.acob.booking.mqttreg.MSG_TYPE_TIMER
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.asTimeString
import com.acob.booking.mqttreg.data.OBMessage
import kotlinx.android.synthetic.main.monitor_list_item.view.*


/**
 * Created by wugang00 on 11/12/2017.
 */
class BlockMonitorListAdaptor(context: Context,lists:List<OBMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var context = context
    var lists = lists
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.monitor_list_item, parent, false)
        return Item(v)

    }

    override fun getItemCount(): Int {
        return lists.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var item = holder as Item
        var itemListener = View.OnClickListener {
           // Log.d(TAG, "come on " +" pos : " + item.itemView.list_event_id.text)


        }
        var msg = lists[position]
        item.bindData(msg)
        when (msg.sender) {
            MSG_TYPE_REGISTER -> {

                holder.itemView.txt_bm_msg.setTextColor(Color.BLACK)
            }
            MSG_TYPE_TIMER -> {
                holder.itemView.txt_bm_msg.setTextColor(Color.parseColor("#0099cc"))
            }
        }



    }


    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(msg: OBMessage) {
//            itemView.list_event_id.text = event.evtId
//            itemView.list_event_name.text = event.name
//            itemView.list_event_desc.text = event.description
//            itemView.list_event_owner.text = event.owner
//            itemView.list_event_start_dt.text = "Start: " + event.startTime?.asString("dd.MM HH:mm")
//            itemView.list_event_end_dt.text = "Start: " + event.endTime?.asString("dd.MM HH:mm")
//            itemView.list_event_deadline.text = "Deadline: " + event.deadline?.asString("dd.MM HH:mm")
            itemView.txt_bm_time.text = msg.time.asTimeString()
            itemView.txt_bm_msg.text = msg.body

        }


    }



}