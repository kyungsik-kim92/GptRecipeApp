package com.example.domain.usecase

import com.example.domain.model.LocalRecipe
import com.example.domain.repo.Repository
import javax.inject.Inject

class FindRecipeByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): LocalRecipe? {
        return repository.findRecipe(id)
    }
}