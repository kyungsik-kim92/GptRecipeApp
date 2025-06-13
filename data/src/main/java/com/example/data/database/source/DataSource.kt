package com.example.data.database.source

import com.example.data.local.entity.LocalRecipeEntity
import com.example.data.remote.dto.GPT
import com.example.data.remote.dto.GptRequestParam
import kotlinx.coroutines.flow.Flow

interface DataSource {
    suspend fun getGptResponse(
        body: GptRequestParam
    ): GPT

    suspend fun getAll(): List<LocalRecipeEntity>

    suspend fun insertRecipe(recipe: LocalRecipeEntity): Long

    suspend fun deleteRecipeByName(recipeName: String)

    suspend fun findRecipeByName(recipeName: String): LocalRecipeEntity?

    suspend fun isFavoriteByName(recipeName: String): Boolean

    suspend fun findRecipe(id: Long): LocalRecipeEntity?

    fun getAllFavoritesFlow(): Flow<List<LocalRecipeEntity>>

}