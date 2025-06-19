package com.example.domain.usecase

import com.example.domain.model.RecipeResponse
import com.example.domain.repo.Repository
import javax.inject.Inject


class GenerateRecipeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(prompt: String): Result<RecipeResponse> {
        return try {
            val response = repository.generateRecipe(prompt)
            if (response.hasError) {
                Result.failure(Exception("Failed to generate recipe: ${response.content}"))
            } else {
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}