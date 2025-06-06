package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.room.dao.RecipeDao
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao
) : DataSource {
    override suspend fun getGptResponse(body: GptRequestParam): GPT {
        return apiService.getGptResponse(body)
    }

    override suspend fun getAll(): List<LocalRecipeEntity> {
        return recipeDao.getAll()
    }

    override suspend fun insertRecipe(recipe: LocalRecipeEntity): Long {
        return recipeDao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(id: Long) {
        return recipeDao.deleteRecipe(id)
    }

    override suspend fun findRecipe(id: Long): LocalRecipeEntity? {
        return recipeDao.findRecipe(id)
    }

    override suspend fun isFavoriteByName(recipeName: String): Boolean {
        return recipeDao.findRecipeByName(recipeName) != null
    }

    override fun getAllFavoritesFlow(): Flow<List<LocalRecipeEntity>> {
        return recipeDao.getAllFavoritesFlow()
    }
}