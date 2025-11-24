package com.example.domain.usecase

import com.example.domain.model.SearchHistory
import com.example.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<SearchHistory>> {
        return repository.getRecentSearches()
    }
}