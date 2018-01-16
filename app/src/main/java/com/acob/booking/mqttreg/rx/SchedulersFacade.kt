package com.acob.booking.mqttreg.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject



/**
 * Created by wugang00 on 10/01/2018.
 */
class SchedulersFacade @Inject constructor() {



    companion object  {

        /**
         * IO thread pool scheduler
         */
        @JvmStatic
        fun io(): Scheduler {
            return Schedulers.io()
        }

        /**
         * Computation thread pool scheduler
         */
        @JvmStatic
        fun computation(): Scheduler {
            return Schedulers.computation()
        }

        /**
         * Main Thread scheduler
         */
        @JvmStatic
        fun ui(): Scheduler {
            return AndroidSchedulers.mainThread()
        }
    }
}