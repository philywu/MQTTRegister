package com.acob.booking.mqttreg.message

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.acob.booking.mqttreg.RECEIVER_MQTT_INTENT
import com.acob.booking.mqttreg.RECEIVER_MQTT_MESSAGE
import com.acob.booking.mqttreg.RECEIVER_MQTT_TOPIC
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.data.OBMessage
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject


/**
 * Created by wugang00 on 12/12/2017.
 */

class MessageProcessor  @Inject constructor(appContext: Context, gson: Gson, lStorage: LocalStorage,
                                            mqttManager: MqttManager) {

    val TAG ="Msg Processor"
    val msgTopicEvent = "acobooking/event" //localStorage.readMessage(KEY_MQTT_TOPIC_EVENT)
   // val msgTopicRegisterPrefix = "acobooking/register/"
    val msgTopicRegisterPrefix = "acobooking/register"
    val msgQos= 1 // localStorage.readMessage(KEY_MQTT_QOS)

    val URL = "tcp://test.mosquitto.org:1883"
    val prefixClient = "acob_booking_client_"
    val clientVersion = "_0001"

    var appContext = appContext
    var gson = gson
    var localStorage = lStorage
    var mqttManager = mqttManager
    var messageList = ArrayList<OBMessage>()


        var mCallback = MqttCallbackBus(this)
        val client_prefix = "acob_client_"


        fun processReceivedMessage(topic: String, message: MqttMessage) {

           var obMsg = gson.fromJson(message.toString(),OBMessage::class.java)
            messageList.add(obMsg)
           // Log.d(TAG,obMsg.toString())

            val intent = Intent(RECEIVER_MQTT_INTENT)
            // Adding some data
            intent.putExtra(RECEIVER_MQTT_MESSAGE, message.toString())
            intent.putExtra(RECEIVER_MQTT_TOPIC, topic)

            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent)

            // var myType = MyMessageType(MyEvent::class.java)

            //var msgWrap = gson.fromJson(message.toString(),MyMessageWrap::class.java)
            /*val className = getClassNameByTopic(topic)

            when (className) {
                OBEvent::class.java.simpleName -> {
                    val myType = MyMessageType(MyMessageWrap::class.java, arrayOf<Type>(OBEvent::class.java))
                    var msgWrap: MyMessageWrap<OBEvent>
                    msgWrap = gson.fromJson(message.toString(), myType)
                    saveEvent(msgWrap.data)
                    var event = msgWrap.data as OBEvent

                    //val topic=msgTopicRegisterPrefix+event.owner+"/"+event.evtId
                    val topic=msgTopicRegisterPrefix
                    messageSubscribe(topic,msgQos)
                    Log.d(TAG, topic + "==== class" + msgWrap.data)
                    nHandler.createEventNotification(msgWrap.data)

                }
                OBRegister::class.java.simpleName -> {
                    val myType = MyMessageType(MyMessageWrap::class.java, arrayOf<Type>(OBRegister::class.java))
                    var msgWrap: MyMessageWrap<OBRegister> = gson.fromJson(message.toString(), myType)
                    saveRegister(msgWrap.data)

                    Log.d(TAG, topic + "==== class" + msgWrap.data)


                }

            }*/



        }




    fun releaseConnection() {
            mqttManager.release()


    }

    fun <T: Any> messagePublish(topic:String, msgObj:T, qos:Int):Boolean{
        if (mqttManager.isConnected()) {

            var jsonStr = gson.toJson(msgObj)

            return mqttManager.publish(topic,qos,jsonStr.toByteArray())

            //var jsonStr = gson.toJson(msgObj)
           // val jsonStr = getJsonString(msgObj)
            //Log.i(TAG,"json: " + jsonStr)
            //mqttManager.publish(topic,qos,jsonStr?.toByteArray())

        } else {
            Log.e(TAG,"not connected")
            return false
        }
    }
    fun messageSubscribe(topic:String, qos:Int): Boolean{
        if (mqttManager.isConnected()) {
            return mqttManager.subscribe(topic,qos)
        } else {
            return false
        }
    }



    fun isServerConnected(): Boolean {
        return mqttManager.isConnected()
    }

    fun connect (URL:String, clientId:String,userName:String?,password:String?):Boolean{
        if (mqttManager.client==null ){
            return  mqttManager.creatConnect(URL,userName,password,clientId,mCallback)
        } else {
            return mqttManager.reConnect()
        }

    }


}