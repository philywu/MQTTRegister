package com.acob.booking.mqttreg.di


import com.acob.booking.mqttreg.MyApplication
import dagger.Component
import javax.inject.Singleton




/**
 * Created by wugang00 on 5/12/2017.
 */
@Singleton
@Component(modules =arrayOf ( AppModule::class,
                                ActivityModule::class ))
interface AppComponent  {

    fun inject(application: MyApplication)

    //fun inject(mqttCallback : MqttCallbackBus)

   // fun localStorage(): LocalStorage
}