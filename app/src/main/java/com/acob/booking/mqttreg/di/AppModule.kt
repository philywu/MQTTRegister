package com.acob.booking.mqttreg.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.acob.booking.mqttreg.data.AppDB
import com.acob.booking.mqttreg.data.LocalStorage
import com.acob.booking.mqttreg.data.SharedPrefStorage
import com.acob.booking.mqttreg.message.MessageProcesserDB
import com.acob.booking.mqttreg.message.MessageProcessor
import com.acob.booking.mqttreg.message.MqttManager
import com.acob.booking.mqttreg.timing.TimeProcessor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import dagger.Module
import dagger.Provides

import javax.inject.Singleton



/**
 * Created by wugang00 on 13/12/2017.
 */
@Module
class AppModule(private val context: Context) {

    var mApplication: Application? = null



     @Provides
    fun providesAppContext() = context

    fun AppModule(application: Application) {
        mApplication = application

/*        var db = providesAppDatabase(context)
        eventDao = db.obEventDao()
        registerDao = db.obRegisterDao()*/
    }

    @Provides
    @Singleton
    fun providesApplication(): Application? {
        return mApplication
    }


/*
    @Provides
    @Singleton
    fun providesMessageProcessor(
            gson : Gson,
            localStorage: LocalStorage,
            mqttManager: MqttManager
    ) = MessageProcessor(context,gson,localStorage,mqttManager)
*/

    @Provides
    @Singleton
    fun providesMessageProcessor(database:AppDB
    ) = MessageProcesserDB(database.obRegisterDao())


    @Provides
    fun providesMqttManager() = MqttManager()


    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyyMMddHHmmss")
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideLocalStorage(): LocalStorage {
        return SharedPrefStorage(context);
    }

    @Provides
    @Singleton
    fun providesTimeProcessor (gson:Gson,localStorage:LocalStorage) = TimeProcessor(context,gson,localStorage)

    //DB
    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDB =
            Room.databaseBuilder(context, AppDB::class.java, "acoregiser-db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun providesOBReigisterDao(database: AppDB) = database.obRegisterDao()




}