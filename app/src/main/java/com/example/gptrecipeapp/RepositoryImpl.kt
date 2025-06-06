package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSource: DataSource) : Repository {
    override suspend fun getGptResponse(body: GptRequestParam): GPT {
        return dataSource.getGptResponse(body)
    }

    override suspend fun isFavoriteByName(recipeName: String): Boolean {
        return dataSource.isFavoriteByName(recipeName)
    }


    override suspend fun getAll(): List<LocalRecipeEntity> {
        return dataSource.getAll()
    }

    override suspend fun insertRecipe(recipe: LocalRecipeEntity): Long {
        return dataSource.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(id: Long) {
        return dataSource.deleteRecipe(id)
    }

    override suspend fun findRecipe(id: Long): LocalRecipeEntity? {
        return dataSource.findRecipe(id)
    }

}