package com.example.gptrecipeapp.room.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gptrecipeapp.room.CustomTypeConverters
import com.example.gptrecipeapp.room.dao.RecipeDao
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity

@Database(entities = [LocalRecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        const val DB_NAME = "recipe.db"
    }
}