package com.hrawat.kotlinnearby

import android.app.Application
import com.orhanobut.hawk.Hawk

/**
 * Created by hrawat on 11/2/2017.
 */
class NearByApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }
}