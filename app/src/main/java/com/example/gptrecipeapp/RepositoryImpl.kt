package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.room.entity.LocalRecipeEntity

class RepositoryImpl(private val dataSource: DataSource) : Repository {
    override suspend fun getGptResponse(body: GptRequestParam): GPT {
        return dataSource.getGptResponse(body)
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