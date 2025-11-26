package com.example.domain.model

data class SearchHistory(
    val id: Long,
    val keyword: String,
    val searchedAt: Long
)