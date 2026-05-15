package com.example.shishu_sneh_healthcare.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "health_records",
    foreignKeys = [
        ForeignKey(
            entity = BabyEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId")]
)
data class HealthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val type: String, // Doctor Visit, Lab Report, Prescription, Illness
    val date: Long,
    val doctorName: String?,
    val clinic: String?,
    val notes: String?,
    val attachmentUri: String? // URI to image or PDF
)
