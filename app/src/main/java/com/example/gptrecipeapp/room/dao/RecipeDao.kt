package com.example.gptrecipeapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity


@Dao
interface RecipeDao {
    @Query("SELECT * FROM LocalRecipeEntity")
    suspend fun getAll(): List<LocalRecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: LocalRecipeEntity): Long

    @Query("DELETE FROM LocalRecipeEntity WHERE id = :id")
    suspend fun deleteRecipe(id: Long)

    @Query("SELECT DISTINCT * FROM LocalRecipeEntity WHERE id = :id")
    suspend fun findRecipe(id: Long): LocalRecipeEntity?

}