package com.example.presentation.model

data class SearchHistoryModel(
    val id: Long,
    val keyword: String,
    val searchedAt: Long
)