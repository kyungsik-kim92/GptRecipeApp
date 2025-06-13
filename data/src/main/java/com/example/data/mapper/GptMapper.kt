package com.example.data.mapper

import com.example.data.remote.dto.GPT
import com.example.domain.model.RecipeResponse

fun GPT.toRecipeResponse(): RecipeResponse {
    val firstChoice = choices.firstOrNull()
    val content = firstChoice?.message?.content ?: ""
    val isComplete = firstChoice?.finish_reason == "stop"

    return RecipeResponse(
        content = content,
        isComplete = isComplete,
        hasError = content.isEmpty()
    )
}