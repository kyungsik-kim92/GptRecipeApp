package com.example.domain.repo

import com.example.domain.model.LocalRecipe
import com.example.domain.model.RecipeResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun generateRecipe(prompt: String): RecipeResponse

    suspend fun isFavoriteByName(recipeName: String): Boolean

    suspend fun getAll(): List<LocalRecipe>

    suspend fun insertRecipe(recipe: LocalRecipe): Long

    suspend fun deleteRecipeByName(recipeName: String)

    suspend fun findRecipe(id: Long): LocalRecipe?

    suspend fun findRecipeByName(recipeName: String): LocalRecipe?

    fun getAllFavoritesFlow(): Flow<List<LocalRecipe>>
}