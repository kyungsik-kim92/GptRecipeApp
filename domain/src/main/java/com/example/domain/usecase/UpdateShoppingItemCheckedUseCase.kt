package com.example.domain.usecase

import com.example.domain.repo.Repository
import javax.inject.Inject

class UpdateShoppingItemCheckedUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(itemId: Long, isChecked: Boolean) {
        repository.updateShoppingItemChecked(itemId, isChecked)
    }
}