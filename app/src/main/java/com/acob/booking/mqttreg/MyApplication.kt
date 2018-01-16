package com.acob.booking.mqttreg

import android.app.Activity
import android.app.Application
import com.acob.booking.mqttreg.di.AppComponent
import com.acob.booking.mqttreg.di.AppModule
import com.acob.booking.mqttreg.di.DaggerAppComponent
import com.acob.booking.mqttreg.rx.SchedulersFacade

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by wugang00 on 13/12/2017.
 */
class MyApplication : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    lateinit var appComponent: AppComponent

    var scheduleFacade :SchedulersFacade = SchedulersFacade()

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()

        appComponent.inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    fun getComponent(): AppComponent {
        return appComponent
    }
}