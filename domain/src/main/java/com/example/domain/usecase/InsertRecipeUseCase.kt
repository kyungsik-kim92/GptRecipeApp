package com.example.domain.usecase

import com.example.domain.model.LocalRecipe
import com.example.domain.repo.Repository
import javax.inject.Inject

class InsertRecipeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(recipe: LocalRecipe): Result<Long> {
        return try {
            val id = repository.insertRecipe(recipe)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}