package com.trustlog.bankfraud.sensor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.trustlog.bankfraud.R

object SensorCollectionManager {

    private const val CHANNEL_ID = "trustlog_session"

    private lateinit var appContext: Context
    private val buffer = SessionBuffer()

    fun initialize(context: Context) {
        appContext = context.applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    fun startForegroundCollection() {
        val intent = Intent(appContext, SensorCollectorService::class.java).apply {
            action = SensorCollectorService.ACTION_START
        }
        ContextCompat.startForegroundService(appContext, intent)
    }

    fun stopForegroundCollection() {
        val intent = Intent(appContext, SensorCollectorService::class.java).apply {
            action = SensorCollectorService.ACTION_STOP
        }
        appContext.stopService(intent)
    }

    internal fun createNotification(): Notification {
        return NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shield)
            .setContentTitle(appContext.getString(R.string.logo_text))
            .setContentText(appContext.getString(R.string.transfer_title))
            .setOngoing(true)
            .build()
    }

    fun logTouch(elementId: String, event: android.view.MotionEvent) {
        buffer.recordTouch(event, elementId)
    }

    fun bufferSensorEvent(type: String, x: Float, y: Float, z: Float) {
        buffer.recordSensorEvent(type, x, y, z)
    }

    fun markRooted(isRooted: Boolean) {
        buffer.markRooted(isRooted)
    }

    fun markOverlayDetected(isDetected: Boolean) {
        buffer.markOverlayDetected(isDetected)
    }

    fun updateAccessibility(list: List<String>) {
        buffer.updateAccessibilityServices(list)
    }

    fun markOtpPasted() {
        buffer.markOtpPasted()
    }

    fun incrementInjectedEvents() {
        buffer.incrementInjectedEvents()
    }

    fun snapshot(): SessionSnapshot = buffer.snapshot()

    fun clear() {
        buffer.clear()
    }
}
