package com.trustlog.bankfraud.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.trustlog.bankfraud.data.model.SessionData
import kotlinx.coroutines.tasks.await

class SessionRepository private constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun uploadSession(session: SessionData) {
        firestore.collection(COLLECTION_NAME)
            .document(session.sessionId)
            .set(session)
            .await()
    }

    companion object {
        private const val COLLECTION_NAME = "trustlog_sessions"

        @Volatile
        private var INSTANCE: SessionRepository? = null

        fun getInstance(): SessionRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionRepository(FirebaseFirestore.getInstance()).also {
                    INSTANCE = it
                }
            }
        }
    }
}
