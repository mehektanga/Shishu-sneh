package com.example.shishu_sneh_healthcare.di

import android.content.Context
import androidx.room.Room
import com.example.shishu_sneh_healthcare.data.local.db.AppDatabase
import com.example.shishu_sneh_healthcare.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // Load the native SQLCipher library
        System.loadLibrary("sqlcipher")

        // In a real app, the passphrase should be securely stored (e.g., in KeyStore)
        val passphrase = "secure_passphrase".toByteArray()
        val factory = SupportOpenHelperFactory(passphrase)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shishu_sneh.db"
        )
            .openHelperFactory(factory)
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }

    @Provides
    fun provideBabyDao(db: AppDatabase): BabyDao = db.babyDao()

    @Provides
    fun provideGrowthDao(db: AppDatabase): GrowthDao = db.growthDao()

    @Provides
    fun provideVaccineDao(db: AppDatabase): VaccineDao = db.vaccineDao()

    @Provides
    fun provideMilestoneDao(db: AppDatabase): MilestoneDao = db.milestoneDao()

    @Provides
    fun provideFeedingDao(db: AppDatabase): FeedingDao = db.feedingDao()

    @Provides
    fun provideHealthRecordDao(db: AppDatabase): HealthRecordDao = db.healthRecordDao()

    @Provides
    fun provideMedicationDao(db: AppDatabase): MedicationDao = db.medicationDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()
}
