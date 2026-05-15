package com.example.shishu_sneh_healthcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "babies")
data class BabyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dob: Long, // Timestamp
    val gender: String,
    val bloodGroup: String,
    val birthWeight: Double,
    val birthHeight: Double,
    val photoUri: String?,
    val motherName: String,
    val pediatrician: String?,
    val hospital: String?,
    val userId: String // Firebase Auth UID
)
