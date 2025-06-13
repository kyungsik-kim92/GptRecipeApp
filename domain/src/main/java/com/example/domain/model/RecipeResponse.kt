package com.example.domain.model

data class RecipeResponse(
    val content: String,
    val isComplete: Boolean,
    val hasError: Boolean = false
)