package com.example.shishu_sneh_healthcare.di

import com.example.shishu_sneh_healthcare.data.repository.*
import com.example.shishu_sneh_healthcare.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBabyRepository(
        babyRepositoryImpl: BabyRepositoryImpl
    ): BabyRepository

    @Binds
    @Singleton
    abstract fun bindGrowthRepository(
        growthRepositoryImpl: GrowthRepositoryImpl
    ): GrowthRepository

    @Binds
    @Singleton
    abstract fun bindVaccineRepository(
        vaccineRepositoryImpl: VaccineRepositoryImpl
    ): VaccineRepository

    @Binds
    @Singleton
    abstract fun bindMilestoneRepository(
        milestoneRepositoryImpl: MilestoneRepositoryImpl
    ): MilestoneRepository

    @Binds
    @Singleton
    abstract fun bindFeedingRepository(
        feedingRepositoryImpl: FeedingRepositoryImpl
    ): FeedingRepository

    @Binds
    @Singleton
    abstract fun bindHealthRecordRepository(
        healthRecordRepositoryImpl: HealthRecordRepositoryImpl
    ): HealthRecordRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
