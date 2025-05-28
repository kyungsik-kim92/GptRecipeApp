package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity

interface DataSource {
    suspend fun getGptResponse(
        body: GptRequestParam
    ): GPT

    suspend fun getAll(): List<LocalRecipeEntity>

    suspend fun insertRecipe(recipe: LocalRecipeEntity): Long

    suspend fun deleteRecipe(id: Long)

    suspend fun findRecipe(id: Long): LocalRecipeEntity?

}