package com.acob.booking.mqttreg.di

import com.acob.booking.mqttreg.ui.MainActivity
import com.acob.booking.mqttreg.MqttMainActivity
import com.acob.booking.mqttreg.RegisterActivity
import com.acob.booking.mqttreg.ui.BlockMonitorActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector



/**
 * Created by wugang00 on 13/12/2017.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeMqttMainActivity(): MqttMainActivity
    @ContributesAndroidInjector
    internal abstract fun contributeRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector
    internal abstract fun contributeBlockMonitorrActivity(): BlockMonitorActivity
}

