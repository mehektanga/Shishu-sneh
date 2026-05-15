package com.example.shishu_sneh_healthcare.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccines",
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
data class VaccineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val name: String,
    val disease: String,
    val scheduledDate: Long,
    val givenDate: Long?,
    val hospital: String?,
    val doctor: String?,
    val status: String, // Upcoming, Due Today, Done, Overdue
    val isSynced: Boolean = false
)
