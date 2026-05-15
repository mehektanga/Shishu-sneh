package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserById(userId: String): Flow<UserEntity?>
    suspend fun insertUser(user: UserEntity): Long
    suspend fun updateUser(user: UserEntity): Int
}
