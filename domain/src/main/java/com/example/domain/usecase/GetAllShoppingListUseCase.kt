package com.example.domain.usecase

import com.example.domain.model.ShoppingItem
import com.example.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllShoppingListUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<ShoppingItem>> {
        return repository.getAllShoppingItems()
    }
}