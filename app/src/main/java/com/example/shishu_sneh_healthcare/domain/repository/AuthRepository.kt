package com.example.shishu_sneh_healthcare.domain.repository

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    fun logout()
    
    suspend fun sendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    )
    
    suspend fun verifyOtp(
        verificationId: String,
        otp: String
    ): kotlinx.coroutines.flow.Flow<Result<Unit>>
}
