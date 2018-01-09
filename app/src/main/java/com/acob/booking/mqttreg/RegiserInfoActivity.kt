package com.acob.booking.mqttreg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_regiser_info.*

import kotlinx.android.synthetic.main.table_row_reg_info.view.*

class RegiserInfoActivity : AppCompatActivity() {
    val TAG = "Reg Info Act"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regiser_info)

        addRows()
        deleteRow ("2 1")
    }

    private fun deleteRow(blcNo: String) {
         var rowCount = details_table.childCount
        lateinit var tr :TableRow
        for (i in 1 until rowCount) {
            tr = details_table.getChildAt(i) as TableRow
            var v = tr.tc_blc_id.text.toString()
            Log.d(TAG,v)
            if (v == blcNo) {
                break
            }
        }
        if (tr!=null) {
            details_table.removeView(tr)
        }
    }

    private fun addRows() {

        for (i in 1..3) {
            val tr = layoutInflater.inflate(R.layout.table_row_reg_info, null) as TableRow
            tr.tc_blc_id.setText("$i 1")
            tr.tc_range.setText("$i 2")
            tr.tc_reg.setText("$i 3")
            tr.tc_vote.setText("$i 4")
            tr.tc_pub.setText("$i 5")
            details_table.addView(tr)
        }
    }
}
