package com.example.shishu_sneh_healthcare.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    // Firebase is disabled for local development.
    // If you need real Firebase features later, restore this file.
}
