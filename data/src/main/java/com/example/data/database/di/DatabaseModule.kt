package com.example.data.database.di

import android.content.Context
import androidx.room.Room
import com.example.gptrecipeapp.ApiService
import com.example.data.database.source.DataSource
import com.example.data.database.source.impl.DataSourceImpl
import com.example.domain.repo.Repository
import com.example.data.repo.RepositoryImpl
import com.example.data.database.dao.RecipeDao
import com.example.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.recipeDao
    }
}

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    @Singleton
    fun provideDataSource(
        apiService: ApiService,
        recipeDao: RecipeDao,
    ): DataSource {
        return DataSourceImpl(apiService, recipeDao)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataSource: DataSource,
    ): Repository {
        return RepositoryImpl(dataSource)
    }
}