package com.example.domain.usecase

import com.example.domain.model.LocalRecipe
import com.example.domain.repo.Repository
import javax.inject.Inject

class GetAllRecipesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): List<LocalRecipe> {
        return repository.getAll()
    }
}