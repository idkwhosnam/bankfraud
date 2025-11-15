package com.trustlog.bankfraud.sensor

import android.view.MotionEvent
import com.trustlog.bankfraud.data.model.PhysicalSensorEvent
import com.trustlog.bankfraud.data.model.SecurityFlags
import com.trustlog.bankfraud.data.model.TouchEvent
import java.util.concurrent.CopyOnWriteArrayList

class SessionBuffer {

    private val touchEvents = CopyOnWriteArrayList<TouchEvent>()
    private val sensorEvents = CopyOnWriteArrayList<PhysicalSensorEvent>()
    @Volatile
    private var securityFlags = SecurityFlags(
        isRooted = false,
        isOverlayDetected = false,
        accessibilityServices = emptyList(),
        otpPasted = false,
        injectedEventsCount = 0
    )

    fun clear() {
        touchEvents.clear()
        sensorEvents.clear()
        securityFlags = securityFlags.copy(
            isRooted = false,
            isOverlayDetected = false,
            accessibilityServices = emptyList(),
            otpPasted = false,
            injectedEventsCount = 0
        )
    }

    fun recordTouch(event: MotionEvent, elementId: String) {
        val pointerIndex = 0
        val timestamp = event.eventTime
        val duration = event.eventTime - event.downTime
        val x = event.getX(pointerIndex)
        val y = event.getY(pointerIndex)
        val pressure = event.getPressure(pointerIndex)
        val size = event.getSize(pointerIndex)
        val viewId = elementId.ifBlank { "unknown" }
        touchEvents.add(
            TouchEvent(
                timestamp = timestamp,
                x = x,
                y = y,
                pressure = pressure,
                size = size,
                duration = duration,
                elementId = viewId
            )
        )
    }

    fun recordSensorEvent(type: String, x: Float, y: Float, z: Float) {
        sensorEvents.add(
            PhysicalSensorEvent(
                timestamp = System.currentTimeMillis(),
                type = type,
                x = x,
                y = y,
                z = z
            )
        )
    }

    fun markRooted(isRooted: Boolean) {
        securityFlags = securityFlags.copy(isRooted = isRooted)
    }

    fun markOverlayDetected(isDetected: Boolean) {
        securityFlags = securityFlags.copy(isOverlayDetected = isDetected)
    }

    fun updateAccessibilityServices(services: List<String>) {
        securityFlags = securityFlags.copy(accessibilityServices = services)
    }

    fun markOtpPasted() {
        securityFlags = securityFlags.copy(otpPasted = true)
    }

    fun incrementInjectedEvents() {
        securityFlags = securityFlags.copy(
            injectedEventsCount = securityFlags.injectedEventsCount + 1
        )
    }

    fun snapshot(): SessionSnapshot {
        return SessionSnapshot(
            touchEvents = touchEvents.toList(),
            sensorEvents = sensorEvents.toList(),
            securityFlags = securityFlags
        )
    }
}
