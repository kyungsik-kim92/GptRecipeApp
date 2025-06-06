package com.example.gptrecipeapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipeDao {
    @Query("SELECT * FROM LocalRecipeEntity")
    suspend fun getAll(): List<LocalRecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: LocalRecipeEntity): Long

    @Query("DELETE FROM LocalRecipeEntity WHERE searchKeyword = :recipeName")
    suspend fun deleteRecipeByName(recipeName: String)

    @Query("SELECT DISTINCT * FROM LocalRecipeEntity WHERE id = :id")
    suspend fun findRecipe(id: Long): LocalRecipeEntity?

    @Query("SELECT * FROM LocalRecipeEntity WHERE searchKeyword = :recipeName LIMIT 1")
    suspend fun findRecipeByName(recipeName: String): LocalRecipeEntity?

    @Query("SELECT * FROM LocalRecipeEntity")
    fun getAllFavoritesFlow(): Flow<List<LocalRecipeEntity>>

}