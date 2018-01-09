package com.acob.booking.mqttreg.message

import android.util.Log

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by wugang00 on 3/12/2017.
 */
@Singleton
class MqttManager @Inject constructor() {

    // 回调
    public val TAG = "MQTT Manager"


    // Private manager variables
    var client: MqttClient? = null
    var conOpt: MqttConnectOptions? = null
    private val clean = true

    init {
        //mCallback = MqttCallbackBus()

    }

    /**
     * 创建Mqtt 连接
     *
     * @param brokerUrl Mqtt服务器地址(tcp://xxxx:1863)
     * @param userName  用户名
     * @param password  密码
     * @param clientId  clientId
     * @return
     */
    fun creatConnect(brokerUrl: String, userName: String?, password: String?, clientId: String,mCallback:MqttCallbackBus): Boolean {
        var flag = false
        val tmpDir = System.getProperty("java.io.tmpdir")
        val dataStore = MqttDefaultFilePersistence(tmpDir)
       // val persistence = MemoryPersistence()
           try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = MqttConnectOptions()
            conOpt!!.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
           // do not set to clean session to keep the subscriber
            conOpt!!.isCleanSession = !clean

            conOpt!!.isAutomaticReconnect = false

            if (password != null) {
                conOpt!!.password = password.toCharArray()
            }
            if (userName != null) {
                conOpt!!.userName = userName
            }

            // Construct an MQTT blocking mode client
            client = MqttClient(brokerUrl, clientId, dataStore)

            // Set this wrapper as the callback handler
            client?.setCallback(mCallback)



            flag = doConnect()
        } catch (e: MqttException) {
            Log.e(TAG,e.message)
        }

        return flag
    }

    /**
     * 建立连接
     *
     * @return
     */
    fun doConnect(): Boolean {
        var flag = false
        if (client != null) {
            try {
                client!!.connect(conOpt)
                Log.d(TAG,"Connected to " + client!!.serverURI + " with client ID " + client!!.clientId)
                flag = true
            } catch (e: Exception) {
                Log.e(TAG,e.message)
                e.printStackTrace()
            }

        }
        return flag
    }

    /**
     * Publish / send a message to an MQTT server
     *
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    fun publish(topicName: String, qos: Int, payload: ByteArray): Boolean {

        var flag = false

        if (client != null && client!!.isConnected) {

            Log.d(TAG,"Publishing to topic \"$topicName\" qos $qos")

            // Create and configure a message
            val message = MqttMessage(payload)
            message.qos = qos

            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client?.publish(topicName, message)
                flag = true
            } catch (e: MqttException) {
                Log.d(TAG,e.toString())
                e.printStackTrace()
            }

        }

        return flag
    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    fun subscribe(topicName: String, qos: Int): Boolean {

        var flag = false

        if (client != null && client!!.isConnected) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For manager if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            Log.d(TAG,"Subscribing to topic \"$topicName\" qos $qos")
            try {
                client!!.subscribe(topicName, qos)

                flag = true
            } catch (e: MqttException) {

            }

        }

        return flag

    }
    fun isConnected(): Boolean{
        return (client != null) && client!!.isConnected
    }

    fun reConnect() : Boolean{
        if (client !=null ) {
            client!!.reconnect()
            return true
        } else {
            return false
        }
    }

    /**
     * 取消连接
     *
     * @throws MqttException
     */
    @Throws(MqttException::class)
    fun disConnect() {
        if (client != null && client!!.isConnected) {
            client!!.disconnect()
            client = null
        }
    }

    private object Holder { val INSTANCE = MqttManager() }
    companion object {

        // 单例
        val mInstance: MqttManager by lazy { Holder.INSTANCE }




    }
    /**
     * 释放单例, 及其所引用的资源
     */
    fun release() {
        try {
            if (mInstance != null) {
                mInstance!!.disConnect()

                Log.i(TAG,"disconnected")
            }
        } catch (e: Exception) {

        }

    }
}
