package com.trustlog.bankfraud.domain

import com.trustlog.bankfraud.sensor.SessionSnapshot
import com.trustlog.bankfraud.ui.SessionResult
import kotlin.math.max

object FraudDetector {

    fun evaluate(snapshot: SessionSnapshot): FraudDecision {
        var score = 10.0
        val flags = snapshot.securityFlags

        if (flags.isRooted) {
            score = 0.0
        }

        if (flags.accessibilityServices.isNotEmpty()) {
            score = 0.0
        }

        if (flags.isOverlayDetected) {
            score -= 5
        }

        if (flags.otpPasted) {
            score -= 3
        }

        if (flags.injectedEventsCount > 0) {
            score -= (flags.injectedEventsCount * 0.5)
        }

        val averageTouchSize = snapshot.touchEvents.map { it.size }.average()
        if (!averageTouchSize.isNaN() && averageTouchSize < 0.1) {
            score -= 4
        }

        if (snapshot.touchEvents.size < 3) {
            score -= 1
        }

        score = max(score, 0.0)
        val result = if (score > 3) SessionResult.SAFE else SessionResult.SUSPECTED
        return FraudDecision(score, result)
    }
}

data class FraudDecision(
    val score: Double,
    val result: SessionResult
)
