package com.example.shishu_sneh_healthcare.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shishu_sneh_healthcare.data.local.dao.BabyDao
import com.example.shishu_sneh_healthcare.data.local.dao.FeedingDao
import com.example.shishu_sneh_healthcare.data.local.dao.GrowthDao
import com.example.shishu_sneh_healthcare.data.local.dao.HealthRecordDao
import com.example.shishu_sneh_healthcare.data.local.dao.MedicationDao
import com.example.shishu_sneh_healthcare.data.local.dao.MilestoneDao
import com.example.shishu_sneh_healthcare.data.local.dao.NotificationDao
import com.example.shishu_sneh_healthcare.data.local.dao.UserDao
import com.example.shishu_sneh_healthcare.data.local.dao.VaccineDao
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import com.example.shishu_sneh_healthcare.data.local.entity.NotificationEntity
import com.example.shishu_sneh_healthcare.data.local.entity.UserEntity
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity

@Database(
    entities = [
        BabyEntity::class,
        GrowthEntryEntity::class,
        VaccineEntity::class,
        MilestoneEntity::class,
        FeedingLogEntity::class,
        HealthRecordEntity::class,
        MedicationEntity::class,
        NotificationEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun babyDao(): BabyDao
    abstract fun growthDao(): GrowthDao
    abstract fun vaccineDao(): VaccineDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun feedingDao(): FeedingDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun medicationDao(): MedicationDao
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao
}
