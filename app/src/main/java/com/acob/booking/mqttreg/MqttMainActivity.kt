package com.acob.booking.mqttreg

import android.content.BroadcastReceiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.data.OBMessage
import com.acob.booking.mqttreg.message.MessageProcessor
import com.google.gson.Gson
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mqtt_main.*
import java.util.*

import javax.inject.Inject


class MqttMainActivity : AppCompatActivity () {

    val TAG = "MQTT Main Act"
    @Inject lateinit var msgProcessor : MessageProcessor
    @Inject lateinit var localStorage : LocalStorage
    @Inject lateinit var gson :Gson

    var user = ""


    private var msgList: ArrayList<OBMessage>? = null
    private var myMessage = true
    lateinit private var listView : ListView
    lateinit private var adapter : ArrayAdapter<OBMessage>

    val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        //inject acitivity to msgProcessor


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mqtt_main)
        user = localStorage.readMessage(KEY_APP_USER_NAME)


        btn_subscribe.setOnClickListener({
            subscribe()
        })
        btn_publish.setOnClickListener({
            publish()
        })
        // init list view
        //msgList = ArrayList<OBMessage>()
        msgList = msgProcessor.messageList.clone() as ArrayList<OBMessage>
        msgProcessor.messageList.clear()

        listView = list_msg
        adapter = MessageAdapter(this, R.layout.left_chat_bubble, msgList!!,user);
        listView.setAdapter(adapter);


        et_message.requestFocus()



        /*if (!msgProcessor.isServerConnected()) {
            connectToMQTTServer()
        }*/
    }

    fun connectToMQTTServer() {


        val clientId = msgProcessor.prefixClient + user + msgProcessor.clientVersion
        val add = compositeDisposable.add(Observable.fromCallable (
                {

                    val b = msgProcessor.connect(msgProcessor.URL, clientId, null, null)
                    //  val b = true
                    Log.d(TAG, "isConnected: " + b)
                    b
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {

                    override fun onNext(isConnected: Boolean) {

                        Log.d(TAG, "conect result: " + isConnected)
                        if (isConnected) {
                            //gotoMQTTMain()
                            subscribe()
                            Toast.makeText(this@MqttMainActivity, "Connected to MQTT Server",Toast.LENGTH_SHORT).show()
                        } else {

                           // finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                      //  showConnectFail()
                    }

                    override fun onComplete() {
                        Log.d(TAG, "done")

                    }
                })
        )
    }

    public override fun onResume() {
        super.onResume()
        // This registers mMessageReceiver to receive messages.

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        IntentFilter(RECEIVER_MQTT_INTENT))
        if (!msgProcessor.isServerConnected()) {
            connectToMQTTServer()
        }
    }

    override fun onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver)
        super.onPause()
    }
    /*override  fun onStop() {
         LocalBroadcastManager.getInstance(this)
               .unregisterReceiver(mMessageReceiver)
        super.onStop()
    }*/
    override fun onDestroy() {
        Log.d(TAG,"activity destroyed")
        msgProcessor.releaseConnection()
        super.onDestroy()

    }
    fun subscribe(){
        val topic = et_topic.text.toString().trim()
        val qos = Integer.parseInt(et_qos.text.toString())

        var isSub = msgProcessor.messageSubscribe(topic,qos)
        tv_sub_status.text = when (isSub) {
            true -> "Subscribed"
            false -> "Not Sub"
        }

    }
    fun publish () {
        val topic = et_topic.text.toString().trim()
        val qos = Integer.parseInt(et_qos.text.toString())
        val msg = et_message.text.toString()
        val obMsg = OBMessage(msg,user, Date())
       val b = msgProcessor.messagePublish(topic,obMsg,qos)
       // val b = true
        val toastMsg = "Message published " + when (b) {
            true -> "successfully"
            false -> "failed"
        }
       // showMessage(obMsg)
        Toast.makeText(this,toastMsg,Toast.LENGTH_LONG).show()
        et_message.setText("", TextView.BufferType.EDITABLE)
    }

    fun displayReceivedMessage(obMsg: OBMessage) {

       // msgList?.clear()
        msgList!!.add(obMsg)
        adapter?.notifyDataSetChanged()
        msgProcessor.messageList.remove(obMsg)
    }
    fun showMessage(obMsg: OBMessage){
        msgList!!.add(obMsg)
        adapter.notifyDataSetChanged();
    }

    // Handling the received Intents for the "my-integer" event
    private val mMessageReceiver = object : BroadcastReceiver() {
        override  fun onReceive(context: Context, intent: Intent) {
            // Extract data included in the Intent
            val msg = intent.getStringExtra(RECEIVER_MQTT_MESSAGE)
            val topic = intent.getStringExtra(RECEIVER_MQTT_TOPIC)
            Log.d("MQTT REC",topic + ":" +msg)


            if (topic == et_topic.text.toString()) {
                var obMsg = gson.fromJson(msg.toString(), OBMessage::class.java)
                displayReceivedMessage(obMsg)
            }

        }
    }
}
