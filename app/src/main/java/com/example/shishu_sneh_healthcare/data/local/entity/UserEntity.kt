package com.example.shishu_sneh_healthcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // Firebase UID
    val phone: String,
    val name: String,
    val role: String, // Mother, Father, ASHA Worker
    val email: String?,
    val photoUri: String?,
    val token: String?,
    val lastSync: Long
)
