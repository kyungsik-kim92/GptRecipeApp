package com.example.domain.usecase

import com.example.domain.model.LocalRecipe
import com.example.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteRecipesFlowUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<LocalRecipe>> {
        return repository.getAllFavoritesFlow()
    }
}