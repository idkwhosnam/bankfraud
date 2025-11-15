package com.trustlog.bankfraud.sensor

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder

class SensorCollectorService : Service(), SensorEventListener {

    companion object {
        const val ACTION_START = "com.trustlog.bankfraud.sensor.START"
        const val ACTION_STOP = "com.trustlog.bankfraud.sensor.STOP"
        private const val SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME
    }

    private var sensorManager: SensorManager? = null

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startCollection()
            ACTION_STOP -> stopCollection()
        }
        return START_STICKY
    }

    private fun startCollection() {
        val notification = SensorCollectionManager.createNotification()
        startForeground(101, notification)
        registerSensor(Sensor.TYPE_ACCELEROMETER)
        registerSensor(Sensor.TYPE_GYROSCOPE)
    }

    private fun stopCollection() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        unregisterSensors()
        stopSelf()
    }

    private fun registerSensor(type: Int) {
        val manager = sensorManager ?: return
        manager.getDefaultSensor(type)?.also { sensor ->
            manager.registerListener(this, sensor, SENSOR_DELAY)
        }
    }

    private fun unregisterSensors() {
        sensorManager?.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSensors()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        val type = when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> "ACCEL"
            Sensor.TYPE_GYROSCOPE -> "GYRO"
            else -> return
        }
        SensorCollectionManager.bufferSensorEvent(type, event.values[0], event.values[1], event.values[2])
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
