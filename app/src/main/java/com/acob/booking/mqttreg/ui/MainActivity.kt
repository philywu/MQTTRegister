package com.acob.booking.mqttreg.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.acob.booking.mqttreg.KEY_APP_USER_NAME
import com.acob.booking.mqttreg.MqttMainActivity
import com.acob.booking.mqttreg.R
import com.acob.booking.mqttreg.RegiserInfoActivity
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.message.MessageProcessor
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var msgProcessor : MessageProcessor
    @Inject lateinit var localStorage : LocalStorage
    val TAG = "MAIN"

    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userName = localStorage.readMessage(KEY_APP_USER_NAME)
        if (userName !=null ) {
            txt_user.setText(userName, TextView.BufferType.EDITABLE)
            txt_user.selectAll()
            txt_user.requestFocus()
        }
        btn_connect.setOnClickListener({
            val txtUserName = txt_user.text.toString().trim()
            var FlagNoUser = false
           when (txtUserName) {
            userName -> {//do nothing
                 }
            "" -> {
                FlagNoUser = true
            }
            else -> {
                   localStorage.writeMessage(KEY_APP_USER_NAME,txtUserName)
               }
           }

            if (FlagNoUser){
                Toast.makeText(this,"Please input User Name",Toast.LENGTH_LONG).show()
                txt_user.requestFocus()
                return@setOnClickListener
            }
            gotoRegisterActivity()

           /* if (msgProcessor.isServerConnected()){
                Log.d(TAG,"already connected")
                gotoMQTTMain()
            } else {
                val clientId = msgProcessor.prefixClient + txtUserName + msgProcessor.clientVersion
                val add = compositeDisposable.add(Observable.fromCallable(
                        {

                            val b = msgProcessor.connect(msgProcessor.URL, clientId, null, null)
                          //  val b = true
                            Log.d(TAG, "isConnected: " + b)
                            b
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {

                            override fun onNext(isConnected: Boolean) {

                                Log.d(TAG, "conect result: " + isConnected)
                                if (isConnected) {
                                    gotoMQTTMain()
                                } else {
                                    showConnectFail()
                                }
                            }

                            override fun onError(e: Throwable) {
                                showConnectFail()
                            }

                            override fun onComplete() {
                                Log.d(TAG, "done")

                            }
                        })
                )
            }*/

        })


    }
    fun showConnectFail() {
        Toast.makeText(this@MainActivity, "Fail to connect MQTT Server", Toast.LENGTH_LONG).show()
    }

    fun gotoMQTTMain() {
        val intent = Intent(this, MqttMainActivity::class.java)
        startActivity(intent);
    }
    fun gotoRegisterActivity(){
        val intent = Intent(this, BlockMonitorActivity::class.java)
        startActivity(intent);
    }

}
