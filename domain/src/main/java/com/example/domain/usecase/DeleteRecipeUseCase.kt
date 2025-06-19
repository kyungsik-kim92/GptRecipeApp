package com.example.domain.usecase

import com.example.domain.repo.Repository
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(recipeName: String): Result<Unit> {
        return try {
            repository.deleteRecipeByName(recipeName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}