package com.example.data.database.di

import com.example.data.remote.firebase.FirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideFirebaseDataSource(): FirebaseDataSource {
        return FirebaseDataSource()
    }
}