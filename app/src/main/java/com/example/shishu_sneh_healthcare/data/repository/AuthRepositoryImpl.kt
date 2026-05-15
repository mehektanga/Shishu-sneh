package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.data.preferences.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    // For local testing, we'll use a hardcoded user ID
    private val MOCK_USER_ID = "mock_user_123"
    private var isMockLoggedIn = false

    override fun isUserLoggedIn(): Boolean {
        // Simple logic to check if user is "logged in" for local dev
        return isMockLoggedIn
    }

    override fun getCurrentUserId(): String? = if (isMockLoggedIn) MOCK_USER_ID else null

    override fun logout() {
        isMockLoggedIn = false
    }

    override suspend fun sendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        // Simulate network delay and always succeed with mock verification ID
        kotlinx.coroutines.delay(1000)
        onCodeSent("mock_verification_id")
    }

    override suspend fun verifyOtp(
        verificationId: String,
        otp: String
    ): Flow<Result<Unit>> = flow {
        // Simulate verification delay
        kotlinx.coroutines.delay(1000)
        
        // Accept any 6-digit OTP for testing
        if (otp.length == 6) {
            isMockLoggedIn = true
            emit(Result.success(Unit))
        } else {
            emit(Result.failure(Exception("Invalid OTP. Use any 6 digits.")))
        }
    }
}
