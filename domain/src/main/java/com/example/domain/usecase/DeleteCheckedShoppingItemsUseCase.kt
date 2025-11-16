package com.example.domain.usecase

import com.example.domain.repo.Repository
import javax.inject.Inject

class DeleteCheckedShoppingItemsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() {
        repository.deleteCheckedShoppingItems()
    }
}