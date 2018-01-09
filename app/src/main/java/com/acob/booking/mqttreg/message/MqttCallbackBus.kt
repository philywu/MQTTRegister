package com.acob.booking.mqttreg.message
import android.util.Log

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

/**
 * Created by wugang00 on 3/12/2017.
 */
class MqttCallbackBus  constructor(p : MessageProcessor):MqttCallbackExtended {
    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        Log.d(TAG,"Connected Complete: " +reconnect + " to "+ serverURI)
    }

    val TAG = "MQTT CallBack"
    var processor = p

/*
    init{
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gson = gsonBuilder.create()
    }
    */

    override fun connectionLost(cause: Throwable) {

        Log.d(TAG,cause.message.toString())
        cause.printStackTrace()
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        Log.d(TAG,topic  + "==== json" + message.toString())
        //var evt = gson.fromJson(message.toString(),MyEvent::class.java)
        processor.processReceivedMessage(topic,message)

       // Log.d(TAG,topic + "==== class" + evt)
       // EventBus.getDefault().post(message)

    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
        Log.d(TAG,"delivery complete")
    }

}
