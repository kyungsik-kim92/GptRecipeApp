package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.database.converters.CustomTypeConverters
import com.example.data.database.dao.RecipeDao
import com.example.data.database.dao.ShoppingListDao
import com.example.data.local.entity.LocalRecipeEntity
import com.example.data.local.entity.ShoppingItemEntity

@Database(
    entities = [
        LocalRecipeEntity::class,
        ShoppingItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val shoppingListDao: ShoppingListDao

    companion object {
        const val DB_NAME = "recipe.db"
    }
}