package com.trustlog.bankfraud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustlog.bankfraud.data.model.SessionData
import com.trustlog.bankfraud.data.repository.SessionRepository
import com.trustlog.bankfraud.domain.FraudDecision
import com.trustlog.bankfraud.domain.FraudDetector
import com.trustlog.bankfraud.sensor.SensorCollectionManager
import com.trustlog.bankfraud.sensor.SessionSnapshot
import com.trustlog.bankfraud.ui.SessionResult
import java.util.UUID
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {

    private val repository = SessionRepository.getInstance()

    private val _resultState = MutableLiveData(ResultUiState())
    val resultState: LiveData<ResultUiState> = _resultState

    private var userEmail: String = ""
    private var userName: String = ""
    private var transferAmount: String = ""
    private var otpCode: String = ""

    fun setUserCredentials(email: String, name: String) {
        userEmail = email
        userName = name
    }

    fun getUserEmail(): String = userEmail

    fun getUserName(): String = userName

    fun setTransferAmount(amount: String) {
        transferAmount = amount
    }

    fun getTransferAmount(): String = transferAmount

    fun onOtpCompleted(code: String) {
        otpCode = code
    }

    fun finalizeTransaction(amount: String) {
        setTransferAmount(amount)
        SensorCollectionManager.stopForegroundCollection()
        val snapshot = SensorCollectionManager.snapshot()
        finalizeSession(snapshot)
    }

    fun finalizeSession(snapshot: SessionSnapshot) {
        viewModelScope.launch {
            _resultState.value = ResultUiState(isUploading = true)
            val decision = FraudDetector.evaluate(snapshot)
            val sessionData = snapshot.toSessionData(
                sessionId = UUID.randomUUID().toString(),
                userEmail = userEmail,
                userName = userName,
                otpCode = otpCode,
                transferAmount = transferAmount,
                decision = decision
            )
            try {
                repository.uploadSession(sessionData)
                _resultState.value = ResultUiState(
                    isUploading = false,
                    score = decision.score,
                    result = decision.result,
                    sessionData = sessionData
                )
            } catch (error: Exception) {
                _resultState.value = ResultUiState(
                    isUploading = false,
                    score = decision.score,
                    result = decision.result,
                    sessionData = sessionData,
                    errorMessage = error.localizedMessage ?: "Unable to upload session"
                )
            }
        }
    }

    fun resetSession() {
        transferAmount = ""
        otpCode = ""
        _resultState.value = ResultUiState()
    }
}

data class ResultUiState(
    val isUploading: Boolean = false,
    val score: Double = 0.0,
    val result: SessionResult? = null,
    val sessionData: SessionData? = null,
    val errorMessage: String? = null
)

fun SessionSnapshot.toSessionData(
    sessionId: String,
    userEmail: String,
    userName: String,
    otpCode: String,
    transferAmount: String,
    decision: FraudDecision
): SessionData {
    return SessionData(
        sessionId = sessionId,
        userEmail = userEmail,
        userName = userName,
        timestamp = System.currentTimeMillis(),
        touchEvents = touchEvents,
        sensorEvents = sensorEvents,
        securityFlags = securityFlags,
        calculatedScore = decision.score,
        result = decision.result.name
    )
}
