package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gptrecipeapp.room.CustomTypeConverters
import com.example.data.database.dao.RecipeDao
import com.example.data.local.entity.LocalRecipeEntity

@Database(entities = [LocalRecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        const val DB_NAME = "recipe.db"
    }
}