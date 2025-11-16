package com.example.domain.usecase

import com.example.domain.model.ShoppingItem
import com.example.domain.repo.Repository
import javax.inject.Inject

class InsertShoppingItemUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(item: ShoppingItem) {
        repository.insertShoppingItem(item)
    }
}