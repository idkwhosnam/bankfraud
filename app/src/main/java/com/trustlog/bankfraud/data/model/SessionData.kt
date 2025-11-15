package com.trustlog.bankfraud.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionData(
    val sessionId: String,
    val userEmail: String,
    val userName: String,
    val timestamp: Long,
    val touchEvents: List<TouchEvent>,
    val sensorEvents: List<PhysicalSensorEvent>,
    val securityFlags: SecurityFlags,
    val calculatedScore: Double,
    val result: String
) : Parcelable

@Parcelize
data class TouchEvent(
    val timestamp: Long,
    val x: Float,
    val y: Float,
    val pressure: Float,
    val size: Float,
    val duration: Long,
    val elementId: String
) : Parcelable

@Parcelize
data class PhysicalSensorEvent(
    val timestamp: Long,
    val type: String,
    val x: Float,
    val y: Float,
    val z: Float
) : Parcelable

@Parcelize
data class SecurityFlags(
    val isRooted: Boolean,
    val isOverlayDetected: Boolean,
    val accessibilityServices: List<String>,
    val otpPasted: Boolean,
    val injectedEventsCount: Int
) : Parcelable
