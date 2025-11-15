package com.trustlog.bankfraud.sensor

import com.trustlog.bankfraud.data.model.PhysicalSensorEvent
import com.trustlog.bankfraud.data.model.SecurityFlags
import com.trustlog.bankfraud.data.model.TouchEvent

data class SessionSnapshot(
    val touchEvents: List<TouchEvent>,
    val sensorEvents: List<PhysicalSensorEvent>,
    val securityFlags: SecurityFlags
) {
    companion object {
        val EMPTY = SessionSnapshot(
            touchEvents = emptyList(),
            sensorEvents = emptyList(),
            securityFlags = SecurityFlags(
                isRooted = false,
                isOverlayDetected = false,
                accessibilityServices = emptyList(),
                otpPasted = false,
                injectedEventsCount = 0
            )
        )
    }
}
