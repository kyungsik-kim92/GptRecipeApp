package com.example.presentation.model

data class GPTResponse(
    val choices: ArrayList<ChoiceResponse> = ArrayList()
)

data class ChoiceResponse(
    val message: MessageResponse = MessageResponse(),
    val finish_reason: String? = null
)

data class MessageResponse(
    val role: String? = null,
    val content: String? = null
)