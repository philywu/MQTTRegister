package com.acob.booking.mqttreg.message

import com.acob.booking.mqttreg.data.dao.OBRegisterDao
import com.acob.booking.mqttreg.data.model.OBRegister
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by wugang00 on 10/01/2018.
 */
class MessageProcesserDB @Inject constructor(registerDao:OBRegisterDao){

    val msgTopicRegisterPrefix = "acobooking/register"
    val msgQos= 1 // localStorage.readMessage(KEY_MQTT_QOS)


    var registerDao = registerDao




    fun <T: Any> messagePublish(topic:String, msgObj:T, qos:Int): Boolean{

        if (topic.startsWith(msgTopicRegisterPrefix)){
            registerDao.insert(msgObj as OBRegister)
            return true
        } else {
            throw IllegalArgumentException("topic cannot be recognized")
        }

    }
}