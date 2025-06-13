package com.example.data.repo

import com.example.data.database.source.DataSource
import com.example.data.local.entity.LocalRecipeEntity
import com.example.data.remote.dto.GPT
import com.example.data.remote.dto.GptRequestParam
import com.example.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
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

    override suspend fun deleteRecipeByName(recipeName: String) {
        return dataSource.deleteRecipeByName(recipeName)
    }

    override suspend fun findRecipe(id: Long): LocalRecipeEntity? {
        return dataSource.findRecipe(id)
    }


    override suspend fun findRecipeByName(recipeName: String): LocalRecipeEntity? {
        return dataSource.findRecipeByName(recipeName)
    }


    override fun getAllFavoritesFlow(): Flow<List<LocalRecipeEntity>> {
        return dataSource.getAllFavoritesFlow()
    }

}