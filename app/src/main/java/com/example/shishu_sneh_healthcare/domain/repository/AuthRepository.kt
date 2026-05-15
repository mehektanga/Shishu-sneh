package com.example.shishu_sneh_healthcare.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    fun logout()
    
    suspend fun loginWithEmail(email: String, password: String): Flow<Result<Unit>>
    suspend fun registerWithEmail(name: String, email: String, password: String): Flow<Result<Unit>>
    suspend fun hasBabyProfile(): Result<Boolean>
}
