package com.example.gptrecipeapp.di

import com.example.data.database.di.ApiKeyProvider
import com.example.gptrecipeapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object ApiKeyModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object ApiKeyModule {

        @Provides
        @Singleton
        fun provideApiKey(): ApiKeyProvider {
            return object : ApiKeyProvider {
                override fun getApiKey(): String = BuildConfig.API_KEY
            }
        }
    }
}