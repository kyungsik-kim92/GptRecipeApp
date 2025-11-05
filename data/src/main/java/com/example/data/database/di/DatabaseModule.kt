package com.example.data.database.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.database.dao.RecipeDao
import com.example.data.database.dao.ShoppingItemDao
import com.example.data.database.network.ApiService
import com.example.data.database.source.DataSource
import com.example.data.database.source.impl.DataSourceImpl
import com.example.data.repo.RepositoryImpl
import com.example.domain.repo.Repository
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

    @Provides
    @Singleton
    fun provideShoppingItemDao(appDatabase: AppDatabase): ShoppingItemDao {
        return appDatabase.shoppingItemDao
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
        shoppingItemDao: ShoppingItemDao
    ): DataSource {
        return DataSourceImpl(apiService, recipeDao, shoppingItemDao)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataSource: DataSource,
    ): Repository {
        return RepositoryImpl(dataSource)
    }
}