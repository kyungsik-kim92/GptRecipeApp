package com.example.data.mapper

import com.example.data.local.entity.SearchHistoryEntity
import com.example.domain.model.SearchHistory

fun SearchHistoryEntity.toDomain(): SearchHistory {
    return SearchHistory(
        id = id,
        keyword = keyword,
        searchedAt = searchedAt
    )
}
fun SearchHistory.toEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        id = id,
        keyword = keyword,
        searchedAt = searchedAt
    )
}
