package com.trustlog.bankfraud

import android.app.Application
import com.trustlog.bankfraud.sensor.SensorCollectionManager

class TrustLogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SensorCollectionManager.initialize(this)
    }
}
