package com.example.presentation.model

data class GPTResponse(
    val choices: List<ChoiceResponse> = emptyList()
)

data class ChoiceResponse(
    val message: MessageResponse = MessageResponse(),
    val finish_reason: String? = null
)

data class MessageResponse(
    val role: String? = null,
    val content: String? = null
)