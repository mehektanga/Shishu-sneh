package com.example.shishu_sneh_healthcare.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "feeding_logs",
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
data class FeedingLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val type: String, // Breastfeed, Formula, Solid
    val startTime: Long,
    val duration: Int, // in minutes
    val amount: Double, // in ml
    val foodItem: String?,
    val notes: String?
)
