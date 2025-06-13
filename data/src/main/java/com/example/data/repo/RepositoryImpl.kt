package com.example.data.repo

import com.example.data.database.source.DataSource
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.mapper.toRecipeResponse
import com.example.data.remote.dto.GptRequestParam
import com.example.data.remote.dto.MessageRequestParam
import com.example.domain.model.LocalRecipe
import com.example.domain.model.RecipeResponse
import com.example.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSource: DataSource) : Repository {
    override suspend fun generateRecipe(prompt: String): RecipeResponse {
        val gptRequest = GptRequestParam(
            model = "gpt-3.5-turbo",
            messages = listOf(
                MessageRequestParam(
                    role = "user",
                    content = prompt
                )
            )
        )
        return try {
            val gptResponse = dataSource.getGptResponse(gptRequest)
            gptResponse.toRecipeResponse()
        } catch (e: Exception) {
            RecipeResponse(
                content = "",
                isComplete = false,
                hasError = true
            )
        }
    }

    override suspend fun isFavoriteByName(recipeName: String): Boolean {
        return dataSource.isFavoriteByName(recipeName)
    }


    override suspend fun getAll(): List<LocalRecipe> {
        return dataSource.getAll().map { it.toDomain() }
    }

    override suspend fun insertRecipe(recipe: LocalRecipe): Long {
        return dataSource.insertRecipe(recipe.toEntity())
    }

    override suspend fun deleteRecipeByName(recipeName: String) {
        return dataSource.deleteRecipeByName(recipeName)
    }

    override suspend fun findRecipe(id: Long): LocalRecipe? {
        return dataSource.findRecipe(id)?.toDomain()
    }


    override suspend fun findRecipeByName(recipeName: String): LocalRecipe? {
        return dataSource.findRecipeByName(recipeName)?.toDomain()
    }


    override fun getAllFavoritesFlow(): Flow<List<LocalRecipe>> {
        return dataSource.getAllFavoritesFlow().map { entityList ->
            entityList.map { it.toDomain() }
        }
    }

}
